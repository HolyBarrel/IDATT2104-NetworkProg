const express = require('express')
const bodyParser = require('body-parser')
const cors = require('cors')
const path = require('path')
const { spawn } = require('child_process');

const app = express()

var currentCode = "";

app.use(cors());

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

app.use(express.static(path.join(__dirname, 'public')));

app.get('/', function(req, res) {
    res.sendFile(path.join(__dirname, '/index.html'));
  });

app.get('/compile', function(req, res) {
  res.json(currentCode);
}); 

app.post('/compile', async function (req, res) {
  const codeInput = req.body.code
  console.log(codeInput);

  const nodeProcess = spawn('node', ['-e', codeInput]);

  let codeOutput = "";

  nodeProcess.stdout.on('data', (data) => {
    codeOutput += data;
  });

  nodeProcess.stderr.on('data', (data) => {
    console.error('stderr: ${data}');
  });

  nodeProcess.on('close', (code) => {
    console.log('child process exited with code ${code}');
    console.log('output: ${codeOutput}');
    currentCode = codeInput;
    res.send(codeOutput);
  });
});

app.listen(5000, () => {
    console.log('Listening on port 5000...')
})

