import { app } from "./firebase/firebase.js";
import {
  getStorage,
  ref,
  uploadBytes,
  getDownloadURL,
  uploadBytesResumable,
} from "https://www.gstatic.com/firebasejs/10.9.0/firebase-storage.js";
const storage = getStorage(app);

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

export { uploadImage };
