//INSPIRATION: https://github.com/andersaus/onlinecompiler
const express = require('express')
const bodyParser = require('body-parser')
const cors = require('cors')
const path = require('path')
const Docker = require('dockerode')
const concat = require('concat-stream')

const app = express()

var docker = new Docker();

app.use(cors());

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

app.use(express.static(path.join(__dirname, 'public')));

app.get('/', function(req, res) {
    res.sendFile(path.join(__dirname, '/index.html'));
  });

//INSPIRATION: https://github.com/andersaus/onlinecompiler

async function dock(image, command) {
  var out
  try {
    var outStream = concat(function (data) {
      out = data.toString()
      console.log(out)
    })
    var dockerData = await docker.run(image, command, outStream)

    var container = dockerData[1]
    await container.remove()
    console.log('container removed')

  }
  catch (err) {
    console.log(err)
  } 
  return out
}

app.post('/compile', async function (req, res) {
  const language = req.body.language
  const codeInput = req.body.code

  var output;
  switch(language) {
    case "js": 
      output = await dock("js-runner", ["node", "-e", codeInput])
      break;
    case "py":
      output = await dock("py-runner", ["python", "-c", codeInput])
      break;
    default: 
      output = "Something wrong.."
      break;
  }

  console.log(output)
  res.send(output)
   
});

app.listen(5000, () => {
    console.log('Listening on port 5000...')
})

