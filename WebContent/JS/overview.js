/*window.addEventListener('load', ()=> {

    let images = document.getElementsByClassName('image');
    images.forEach(makeButton);

    function makeButton(item) {
        item.addEventListener('click', () => {

            let annotationDiv = document.getElementById("annotation-modal-box");
            annotationDiv.style.display = 'block';
            document.getElementById('image-id').value = item.getAttribute('id');
            document.getElementById('create-annotation').style.display = 'block';


        });
    }
});*/

document.querySelector('body').addEventListener('click', function(e) {
    if (e.target.tagName.toLowerCase() === 'img') {
        let image = e.target;
        document.getElementById("image-id").value = image.getAttribute('id'); // TODO: enter attribute


    }
});

// Add eventListener on close annotation form button
document.getElementById('annotation-close').addEventListener('click', ()=>{

    let annotationDiv= document.getElementById('annotation-modal-box');
    annotationDiv.style.display='none'

});

// Add eventListener on window to close the modal boxes when there is a click outside them
window.addEventListener('click', (e)=> {

    let annotationDiv = document.getElementById('annotation-modal-box');

    if (e.target === annotationDiv) {
        annotationDiv.style.display = 'none';
    }
});

