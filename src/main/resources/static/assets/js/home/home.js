import { app } from "../firebase/firebase.js";
import {
  getStorage,
  ref,
  uploadBytes,
  getDownloadURL,
  uploadBytesResumable,
} from "https://www.gstatic.com/firebasejs/10.9.0/firebase-storage.js";
import * as Intercept from "../Interceptor/intercept.js";

console.log(Cookies);
const btnImage = document.getElementById("btn-image");
const inputImg = btnImage.querySelector("#image");
const btnSubmit = document.querySelector('button[type="submit"]');
const createPost = document.getElementById("post-modal");
console.log(createPost);

const storage = getStorage(app);

console.log(storage);

btnImage.addEventListener("click", () => {
  inputImg.click();
});

btnSubmit.addEventListener("click", (e) => {
  e.preventDefault();
  console.log(inputImg.files.length);
  if (inputImg.files.length > 0) {
    handleFormSubmit();
  }
});
console.log(app);

async function handleFormSubmit(token = null) {
  console.log("herelll");
  const url = await uploadImage(inputImg.files[0]);
  let text = createPost.querySelector('input[type="text"]').value;
  // post api
  axios
    .post("http://localhost:8080/api/v1/posts", {
      postText: text,
      postImage: url,
    })
    .then(function (response) {
      console.log(response);
      createPost.style.display = "none";
      const body = document.querySelector("body");
      body.style = null;
      body.class = null;
      createPost.class = null;
      document.querySelector(".modal-backdrop").remove();
    })
    .catch(function (error) {
      console.log(error);
    });
}

function renderPost(listPost) {}

// tạo một component riêng [refactor]
function uploadImage(file) {
  return new Promise((resolve, reject) => {
    const metadata = {
      contentType: file.type,
    };
    const fileName = Date.now();
    console.log(fileName);

    const storageRef = ref(storage, `images/${fileName}`);
    const uploadTask = uploadBytesResumable(storageRef, file, metadata);

    uploadTask.on(
      "onStateChanged",
      (snapshot) => {
        // Get task progress, including the number of bytes uploaded and the total number of bytes to be uploaded
        const progress =
          (snapshot.bytesTransferred / snapshot.totalBytes) * 100;
        console.log("Upload is " + progress + "% done");
        switch (snapshot.state) {
          case "paused":
            console.log("Upload is paused");
            break;
          case "running":
            console.log("Upload is running");
            break;
        }
      },
      (error) => {
        reject(`Có lỗi ở upload ảnh lên firebase: ` + error);
      },
      () => {
        // Upload completed successfully, now we can get the download URL
        getDownloadURL(uploadTask.snapshot.ref).then((downloadURL) => {
          console.log("File available at", downloadURL);
          resolve(downloadURL);
        });
      }
    );
  });
}
