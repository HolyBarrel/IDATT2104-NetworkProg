const crypto = require('crypto')
const net = require('net');

//Template code origin: https://folk.ntnu.no/eidheim/network_programming/websocket/task.html

// Simple HTTP server responds with a simple WebSocket client test
const httpServer = net.createServer((connection) => {
  connection.on('data', () => {
    let content = `<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
  </head>
  <body>
    WebSocket test page
    <script>
      let ws = new WebSocket('ws://localhost:3001');
      ws.onmessage = event => console.log('Message from server: ' + event.data);
      ws.onopen = () => {
        console.log('Handshake established');
        ws.send('Hellå');
      };
    </script>
  </body>
</html>
`;
    connection.write('HTTP/1.1 200 OK\r\nContent-Length: ' + content.length + '\r\n\r\n' + content);
  });
});
httpServer.listen(3000, () => {
  console.log('HTTP server listening on port 3000');
});

var clientList = []

// SOURCES
// https://nodejs.org/api/crypto.html#cryptocreatehashalgorithm-options
// https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API/Writing_WebSocket_servers
// https://www.rfc-editor.org/rfc/rfc6455

const wsServer = net.createServer((connection) => {
  let secWebSockKey = "";

  clientList.push(connection)

  console.log('Client connected \n Total client count: ' + clientList.length);

  connection.on('data', (data) => {
    console.log('Data received from client: ', data.toString());

  if (data.toString().startsWith('GET /')) {
    const match = /Sec-WebSocket-Key:\s*(.*)\r\n/.exec(data.toString());

    if (match !== null && secWebSockKey === '') {
      secWebSockKey = match[1];
      //console.log("EXTRACTED KEY: " + secWebSockKey);

      const magicKey = secWebSockKey + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
      //console.log("MAGIC STRING: " + magicKey);

      var hashKey = crypto.createHash('sha1');
      hashKey.update(magicKey);
      const secWebSockAccept = hashKey.digest('base64');
      //console.log(secWebSockAccept);

      connection.write(
        'HTTP/1.1 101 Switching Protocols\r\nUpgrade: websocket\r\nConnection: Upgrade\r\nSec-WebSocket-Accept: ' + secWebSockAccept + '\r\n\r\n'
      );
    }
  } else {
    //SOURCE: https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API/Writing_WebSocket_servers
    //INSPIRATION: https://www.youtube.com/watch?v=qFoFKLI3O8w

    console.log("Decoded message: " + decodeBytes(data))

  }

  broadcastResponse()

  })

  connection.on('end', () => {
    clientList.splice(clientList.indexOf(connection), 1)
    console.log('Client disconnected \n Total client count: ' + clientList.length)

  })
})

wsServer.on('error', (error) => {
  console.error('Error: ', error);
})

wsServer.listen(3001, () => {
  console.log('WebSocket server listening on port 3001')
})

function decodeBytes(data) {

  //Extracting bytes to byte-array
  var byteArray = [data.length]

  for (let i = 0; i < data.length; i++) {
    const byte = data.readUInt8(i)
    byteArray[i] = byte
  }

  //Removing masked bit manually
  const LENGTH_ID = byteArray[1] - 128  

  var DATA_OFFSET

  var DATA_LENGTH

  var MASK_OFFSET
  
  switch(true) {
    case (LENGTH_ID <= 125): 
      
      MASK_OFFSET = 2
      
      DATA_LENGTH = LENGTH_ID

      DATA_OFFSET = 6

      break
    case (LENGTH_ID === 126):
    
      MASK_OFFSET = 4

      //Reads the third and fourth byte as a 16 bit unsigned integer
      DATA_LENGTH = data.slice(2, 4).readUInt16BE()

      DATA_OFFSET = 8

      break
      //Conceptual --> hard to test
    case(LENGTH_ID > 126):

      MASK_OFFSET = 6

      //Reads the third and fourth byte as a 16 bit unsigned integer
      DATA_LENGTH = data.slice(2, 8).readUInt64BE()

      if(DATA_LENGTH > 65535) {
        console.log("Please ensure that the payload is smaller than MAX_SIZE for Websocket (2^63 - 1 == 65536 bytes)")
        break
      }

      DATA_OFFSET = 10

      break
    default: 
      console.log("There was an error, please ensure that the payload is smaller than MAX_SIZE for Websocket (2^63 - 1 == 65536 bytes)")
      break
  }

  //Manual extraction of mask key
  var MASK_KEY = [
    byteArray[MASK_OFFSET],
    byteArray[MASK_OFFSET + 1], 
    byteArray[MASK_OFFSET + 2], 
    byteArray[MASK_OFFSET + 3]
  ] 

  const DECODED_BYTES = []
  for (let i = 0; i < DATA_LENGTH; i++) {
    const byte = data.readUInt8(i + DATA_OFFSET) ^ MASK_KEY[i % 4]
    DECODED_BYTES.push(byte)
  }

  return Buffer.from(DECODED_BYTES).toString('utf8');
}

function broadcastResponse() {
  //Sending response from server to client(s)
  const response = "Hello from server"

  //Allocating the message in accordance with WebSocket
  const returnBuffer = Buffer.alloc(2 + response.length)
  returnBuffer[0] = 0b10000001
  returnBuffer[1] = response.length

  returnBuffer.write(response, 2)
  
  clientList.forEach(connection => {
    connection.write(returnBuffer)
  });
}