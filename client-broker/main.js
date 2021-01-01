var heroku_url = 'spring-socket.herokuapp.com/';
var local_url = 'localhost:8080';
var stompClient = null;
var firstTime = false;
var connected = false;
var user_name = null;

var files = [];

const inputFile = document.getElementById('upload');
const userForm = document.getElementById('userForm');
const fileArea = document.getElementById('fileArea');
const connectedElement = document.getElementById('connect');
const conectedText = document.getElementById('conected-text');

const filesContainer = document.getElementById('filesContainer');


userForm.addEventListener('submit', connect, true);

function selectFile(target) {
  document.getElementById('file-name').innerHTML = target.files[0].name;
}


function _generateId(value) {
  let prime = 31;
  let result = 1;
  result =
    prime * result + Math.floor((1 + Math.random()) * 0x10000).toString(16);
  return btoa(result + '\\' + value + '\\' + user_name);
}

function connect(event) {
  let socket = new SockJS(`http://${local_url}/ws`);

  stompClient = Stomp.over(socket);

  stompClient.connect({}, onConnect, onError);
  event.preventDefault();
}

function sendMessage() {
  // console.log('send a message test to broker');
  const { files } = inputFile;

  if (files.length === 0) {
    alert('file not found! \n Select a File');
    throw new Error('File not found');
  }
  let file = files[0];
  // console.log({ file: file });

  let sessionId = _generateId(file.name);

  if (!connected) {
    alert('Você não está conectado!');
    throw new String('necessário estar conectado');
  }
  let socketFiles = new WebSocket(
    `ws://${local_url}/upload?sessionId=${sessionId}`
  );
  socketFiles.onopen = () => {
    console.log('connected to upload socket');
    socketFiles.send(file);
    console.log('SocketClient finished sending file:  ' + file.name);
  };
  socketFiles.onmessage = (response) => {
    console.log('SocketClient response: ', response);
    socketFiles.close();
  };
  stompClient.send(
    '/app/upload.sendData',
    {},
    JSON.stringify(file.name) // maybe send the userID to get files in server and return
  );
}

function onConnect(data) {
  user_name = data.headers['user-name'];
  console.log('connected');
  connected = true;
  connectedElement.classList.remove('not-connected'); // remove old css
  connectedElement.classList.add('connected');

  conectedText.textContent = 'CONECTADO';
  stompClient.subscribe('/topic/public', onMessage);
  stompClient.subscribe('/user/queue/reply', onSyncMessage);
}
function onSyncMessage(message) {
  const body = JSON.parse(message.body);
  console.log({ mensagem: body });
  // if (message != undefined && files != undefined) {
  //   if (!message.body.length === files.body.length) {
  //     createFiles(body);
  //   }
  // }
  if (body) {
    if (firstTime) {
      console.log(fileArea.firstChild);
      console.log('remove fisrt');
      fileArea.removeChild(fileArea.getElementsByTagName('h2')[0]);
    }
    createFiles(body);
  }
}

function createFiles(bodyObject) {
  console.log({ receivedCreate: bodyObject });
  verifyTypeResponse(bodyObject)
    ? saveFilesArray(bodyObject)
    : saveFilesObject(bodyObject);
}
function createListFile() {
  console.log({ files: files });
  var lis = document.querySelectorAll('#fileArea li');
  for (var i = 0; (li = lis[i]); i++) {
    li.parentNode.removeChild(li);
  }

  files.map((el) => {
    console.log(el);
    filesContainer.classList.remove('files-area-center');
    let fileElement = document.createElement('li');
    let nameFileElement = document.createTextNode(el.fileName);
    fileElement.appendChild(nameFileElement);
    fileElement.classList.add('files-received');
    // fileElement.n;
    fileArea.appendChild(fileElement);
    fileArea.scrollTop = fileArea.scrollHeight;
    firstTime = false;
  });
}
function saveFilesObject(bodyObject) {
  console.log({ saveObject: bodyObject });

  files = files.concat([{ ...bodyObject }]);
  console.log({ FILE: files });
  createListFile();
}
Array.prototype.unique = function () {
  return Array.from(new Set(this));
};
function saveFilesArray(bodyObject) {
  let bodyFixedNames = bodyObject.map((el) => {
    return {
      ...el,
      fileName: (el.fileName = el.fileName.replaceAll('%20', ' ')),
    };
  });

  files = [];

  files = files.concat(bodyFixedNames);
  // files = bodyObject.unique();

  // files = files.filter((value, index) => files.indexOf(value) != index);
  console.log({ bodyFixed: bodyFixedNames });

  // if (files.length > 0) {
  //   files = bodyFixedNames.map((sync) => {
  //     return files.filter((el) => el.fileName !== sync.fileName);
  //   });
  // } else {
  //   files = files.concat(
  //     bodyObject.map((el) => {
  //       return {
  //         ...el,
  //         fileName: (el.fileName = el.fileName.replaceAll('%20', ' ')),
  //       };
  //     })
  //   );
  // }

  console.log({ filesT: files });
  // files = files.concat(
  //   bodyObject.map((el) => {
  //     return {
  //       ...el,
  //       fileName: (el.fileName = el.fileName.replaceAll('%20', ' ')),
  //     };
  //   })
  // );
  createListFile();
}
function onError() {
  connectedElement.classList.remove('connected'); // remove old css
  connectedElement.classList.add('not-connected');
  connected = false;
  conectedText.textContent = 'DESCONECTADO';
  console.log('Could not connect to WebSocket server.');
}
function onMessage(message) {
  const body = JSON.parse(message.body);
  console.log({ messageADD: body });
  // console.log('firstTIME', firstTime);
  if (body) {
    if (firstTime) {
      console.log(fileArea.firstChild);
      console.log('remove fisrt');
      fileArea.removeChild(fileArea.getElementsByTagName('h2')[0]);
    }
    // filesContainer.classList.remove('files-area-center');

    // let fileElement = document.createElement('li');

    // let nameFileElement = document.createTextNode(bodyObject.file);

    // fileElement.appendChild(nameFileElement);
    // fileElement.classList.add('files-received');
    // // fileElement.n;

    // fileArea.appendChild(fileElement);
    // fileArea.scrollTop = fileArea.scrollHeight;
    // firstTime = false;

    createFiles(body);
  }
}

function sycronize() {
  console.log('Request syncronize');
  stompClient.send('/app/upload.syncData', {}, 'GET'); // get files updated
}
document.addEventListener('DOMContentLoaded', theDomHasLoaded, false);

function theDomHasLoaded(e) {
  firstTime = true;
  console.log(e);
  console.log('running');
  connect(e);
  filesContainer.classList.add('files-area-center');
  const element = document.createElement('h2');
  element.textContent = 'Sem arquivos para mostrar';
  fileArea.appendChild(element);
}

// (function main() {
//   // console.log('running');
//   // connect();

//   fileArea.classList.add('files-area-center');
//   const element = (document.createElement('h2').textContent = 'Sem arquivos');
//   fileArea.appendChild(element);
// })();

function verifyTypeResponse(data) {
  return Array.isArray(data);
}
