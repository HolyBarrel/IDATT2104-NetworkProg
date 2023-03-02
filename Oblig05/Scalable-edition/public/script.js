document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("compile_btn").addEventListener("click", compileCodeInput)

})

function compileCodeInput() {
   const codeInput = document.getElementById("code").value;
   const langInput = document.getElementById("language").value;
   console.log(langInput)
   axios.post('/compile', {
    code: codeInput,
    language: langInput
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