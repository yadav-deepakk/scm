console.log("Img preview script has been loaded...");

var contactImage = document.querySelector("#contactImage");
var imgPreviewDiv = document.querySelector("#img-preview-div");
var imgPreview = document.querySelector("#img-preview");

// show image preview and close button as soon as file is being uploaded. 
contactImage.addEventListener("change", event => {
    imgPreviewDiv.classList.remove("hidden");
    var file = event.target.files[0];
    var reader = new FileReader();
    reader.onload = function () {
        imgPreview.setAttribute("src", reader.result)
    };
    reader.readAsDataURL(file);
});

// close image preview as soon as image preview close button clicked. 
document.querySelector("#img-preview-close").addEventListener("click", event => {
    console.log("image preview closed.");
    contactImage.value = "";
    imgPreviewDiv.classList.add("hidden");
});