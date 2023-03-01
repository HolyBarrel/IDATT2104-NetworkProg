document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("compile_btn").addEventListener("click", compileCodeInput)

})

function compileCodeInput() {
   const codeInput = document.getElementById("code").value;
   console.log(codeInput)
   axios.post('/compile', {
    code: codeInput
   })
   .then(function (response) {
    const output = document.getElementById('output');
    console.log(response.data.code)
    output.value = response.data;
  })
  .catch(function (error) {
    console.error(error);
  });
}