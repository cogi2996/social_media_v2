import { app } from "../firebase/firebase.js";
import {
  getStorage,
  ref,
  uploadBytes,
  getDownloadURL,
  uploadBytesResumable,
} from "https://www.gstatic.com/firebasejs/10.9.0/firebase-storage.js";
console.log(Cookies);
const btnImage = document.getElementById("btn-image");
const inputImg = btnImage.querySelector("#image");
const btnSubmit = document.querySelector('button[type="submit"]');
const createPost = document.getElementById("post-modal");
const containerPost = document.getElementById("container__post");
console.log(createPost);

const storage = getStorage(app);

console.log(storage);
getListNewPost(0, 3);
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

window.addEventListener("scroll", () => {
  console.log(window.scrollY); //scrolled from top
  console.log(window.innerHeight); //visible part of screen
  if (
    window.scrollY + window.innerHeight + 1 >=
    document.documentElement.scrollHeight
  ) {
    const totalCurNumPost = containerPost.querySelectorAll(
      ".col-sm-12[data-post-id]"
    );
    let pageSize = 3;
    let pageNum = 0;
    if (totalCurNumPost.length === 0) pageNum = 0;
    else pageNum = totalCurNumPost.length / pageSize;
    getListNewPost(pageNum, pageSize);
  }
});

function getListNewPost(pageNum = 0, pageSize = 2) {
  axios
    .get(`/api/v1/posts?pageNum=${pageNum}&pageSize=${pageSize}`)
    .then(({ data }) => {
      data.forEach((post) => {
        renderPost(post);
      });
    })
    .catch(function (error) {
      console.log(error);
    });
}

function renderPost(post) {
  const createTime = formatDate(new Date(post.postCreateTime));
  const avatar =
    post.userDTO.avatar === null
      ? `/assets/images/user/defaul_avatar.jpg`
      : post.userDTO.avatar;
  let html = `<div class="col-sm-12" data-post-id = ${post.postId}>
  <div class="card card-block card-stretch card-height">
    <div class="card-body">
      <div class="user-post-data">
        <div class="d-flex justify-content-between">
          <div class="me-3">
            <img class="rounded-circle avatar-50" src="${avatar}" alt="">
          </div>
          <div class="w-100">
            <div class="d-flex justify-content-between">
              <div class="">
                <a href="/profile?id=${post.userDTO.userId}">
                  <h5 class="mb-0 d-inline-block">
                    ${post.userDTO.lastName} ${post.userDTO.midName} ${post.userDTO.firstName}
                  </h5>
                </a>
                <span class="mb-0 d-inline-block">đã thêm một bài viết mới</span>
                <p class="mb-0 text-primary">${createTime}</p>
              </div>
              <div class="card-post-toolbar">
                <div class="dropdown">
                  <span class="dropdown-toggle" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false" role="button">
                    <i class="ri-more-fill"></i>
                  </span>
                  <div class="dropdown-menu m-0 p-0">
                    <a class="dropdown-item p-3" href="#">
                      <div class="d-flex align-items-top">
                        <div class="h4">
                          <i class="ri-save-line"></i>
                        </div>
                        <div class="data ms-2">
                          <h6>Save Post</h6>
                          <p class="mb-0">
                            Add this to your saved items
                          </p>
                        </div>
                      </div>
                    </a>
                    <a class="dropdown-item p-3" href="#">
                      <div class="d-flex align-items-top">
                        <i class="ri-close-circle-line h4"></i>
                        <div class="data ms-2">
                          <h6>Hide Post</h6>
                          <p class="mb-0">
                            See fewer posts like this.
                          </p>
                        </div>
                      </div>
                    </a>
                    <a class="dropdown-item p-3" href="#">
                      <div class="d-flex align-items-top">
                        <i class="ri-user-unfollow-line h4"></i>
                        <div class="data ms-2">
                          <h6>Unfollow User</h6>
                          <p class="mb-0">
                            Stop seeing posts but stay friends.
                          </p>
                        </div>
                      </div>
                    </a>
                    <a class="dropdown-item p-3" href="#">
                      <div class="d-flex align-items-top">
                        <i class="ri-notification-line h4"></i>
                        <div class="data ms-2">
                          <h6>Notifications</h6>
                          <p class="mb-0">
                            Turn on notifications for this post
                          </p>
                        </div>
                      </div>
                    </a>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="mt-3">
        <p>
          ${post.postText}
        </p>
      </div>
      <div class="user-post">
        <a href="javascript:void();"><img src="${post.postImage}" alt="post-image" class="img-fluid rounded w-100"></a>
      </div>
      <div class="comment-area mt-3">
        <div class="d-flex justify-content-between align-items-center flex-wrap">
          <div class="like-block position-relative d-flex align-items-center">
            <div class="d-flex align-items-center">
              <div class="like-data">
                <div class="dropdown">
                  <span class="dropdown-toggle" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false" role="button">
                    <img src="../assets/images/icon/01.png" class="img-fluid" alt="">
                  </span>
                  <div class="dropdown-menu py-2">
                    <a class="ms-2 me-2" href="#" data-bs-toggle="tooltip" data-bs-placement="top" title="" data-bs-original-title="Like" aria-label="Like"><img src="../assets/images/icon/01.png" class="img-fluid" alt=""></a>
                    <a class="me-2" href="#" data-bs-toggle="tooltip" data-bs-placement="top" title="" data-bs-original-title="Love" aria-label="Love"><img src="../assets/images/icon/02.png" class="img-fluid" alt=""></a>
                    <a class="me-2" href="#" data-bs-toggle="tooltip" data-bs-placement="top" title="" data-bs-original-title="Happy" aria-label="Happy"><img src="../assets/images/icon/03.png" class="img-fluid" alt=""></a>
                    <a class="me-2" href="#" data-bs-toggle="tooltip" data-bs-placement="top" title="" data-bs-original-title="HaHa" aria-label="HaHa"><img src="../assets/images/icon/04.png" class="img-fluid" alt=""></a>
                    <a class="me-2" href="#" data-bs-toggle="tooltip" data-bs-placement="top" title="" data-bs-original-title="Think" aria-label="Think"><img src="../assets/images/icon/05.png" class="img-fluid" alt=""></a>
                    <a class="me-2" href="#" data-bs-toggle="tooltip" data-bs-placement="top" title="" data-bs-original-title="Sade" aria-label="Sade"><img src="../assets/images/icon/06.png" class="img-fluid" alt=""></a>
                    <a class="me-2" href="#" data-bs-toggle="tooltip" data-bs-placement="top" title="" data-bs-original-title="Lovely" aria-label="Lovely"><img src="../assets/images/icon/07.png" class="img-fluid" alt=""></a>
                  </div>
                </div>
              </div>
              <div class="total-like-block ms-2 me-3">
                <div class="dropdown">
                  <span class="dropdown-toggle" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false" role="button">
                    140 Likes
                  </span>
                  <div class="dropdown-menu">
                    <a class="dropdown-item" href="#">Max Emum</a>
                    <a class="dropdown-item" href="#">Bill Yerds</a>
                    <a class="dropdown-item" href="#">Hap E. Birthday</a>
                    <a class="dropdown-item" href="#">Tara Misu</a>
                    <a class="dropdown-item" href="#">Midge Itz</a>
                    <a class="dropdown-item" href="#">Sal Vidge</a>
                    <a class="dropdown-item" href="#">Other</a>
                  </div>
                </div>
              </div>
            </div>
            <div class="total-comment-block">
              <div class="dropdown">
                <span class="dropdown-toggle" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false" role="button">
                  20 Comment
                </span>
                <div class="dropdown-menu">
                  <a class="dropdown-item" href="#">Max Emum</a>
                  <a class="dropdown-item" href="#">Bill Yerds</a>
                  <a class="dropdown-item" href="#">Hap E. Birthday</a>
                  <a class="dropdown-item" href="#">Tara Misu</a>
                  <a class="dropdown-item" href="#">Midge Itz</a>
                  <a class="dropdown-item" href="#">Sal Vidge</a>
                  <a class="dropdown-item" href="#">Other</a>
                </div>
              </div>
            </div>
          </div>
          <div class="share-block d-flex align-items-center feather-icon mt-2 mt-md-0">
            <a href="javascript:void();" data-bs-toggle="offcanvas" data-bs-target="#share-btn" aria-controls="share-btn"><i class="ri-share-line"></i>
              <span class="ms-1">99 Share</span></a>
          </div>
        </div>
        <hr>
        <ul class="post-comments list-inline p-0 m-0">
          <li class="mb-2">
            <div class="d-flex">
              <div class="user-img">
                <img src="../assets/images/user/02.jpg" alt="userimg" class="avatar-35 rounded-circle img-fluid">
              </div>
              <div class="comment-data-block ms-3">
                <h6>Monty Carlo</h6>
                <p class="mb-0">Lorem ipsum dolor sit amet</p>
                <div class="d-flex flex-wrap align-items-center comment-activity">
                  <a href="javascript:void();">like</a>
                  <a href="javascript:void();">reply</a>
                  <a href="javascript:void();">translate</a>
                  <span> 5 min </span>
                </div>
              </div>
            </div>
          </li>
          <li>
            <div class="d-flex">
              <div class="user-img">
                <img src="../assets/images/user/03.jpg" alt="userimg" class="avatar-35 rounded-circle img-fluid">
              </div>
              <div class="comment-data-block ms-3">
                <h6>Paul Molive</h6>
                <p class="mb-0">Lorem ipsum dolor sit amet</p>
                <div class="d-flex flex-wrap align-items-center comment-activity">
                  <a href="javascript:void();">like</a>
                  <a href="javascript:void();">reply</a>
                  <a href="javascript:void();">translate</a>
                  <span> 5 min </span>
                </div>
              </div>
            </div>
          </li>
        </ul>
        <form class="comment-text d-flex align-items-center mt-3" action="javascript:void(0);">
          <input type="text" class="form-control rounded" placeholder="Enter Your Comment">
          <div class="comment-attagement d-flex">
            <a href="javascript:void();"><i class="ri-link me-3"></i></a>
            <a href="javascript:void();"><i class="ri-user-smile-line me-3"></i></a>
            <a href="javascript:void();"><i class="ri-camera-line me-3"></i></a>
          </div>
        </form>
      </div>
    </div>
  </div>
  </div>`;
  containerPost.insertAdjacentHTML("beforeend", html);
  // insert data into each post
  // listPost.forEach((post) => {
  //   let html = `<div class="col-sm-12">
  //   <div class="card card-block card-stretch card-height">
  //     <div class="card-body">
  //       <div class="user-post-data">
  //         <div class="d-flex justify-content-between">
  //           <div class="me-3">
  //             <img class="rounded-circle img-fluid" src="../assets/images/user/01.jpg" alt="">
  //           </div>
  //           <div class="w-100">
  //             <div class="d-flex justify-content-between">
  //               <div class="">
  //                 <h5 class="mb-0 d-inline-block">Anna Sthesia</h5>
  //                 <span class="mb-0 d-inline-block">Add New Post</span>
  //                 <p class="mb-0 text-primary">Just Now</p>
  //               </div>
  //               <div class="card-post-toolbar">
  //                 <div class="dropdown">
  //                   <span class="dropdown-toggle" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false" role="button">
  //                     <i class="ri-more-fill"></i>
  //                   </span>
  //                   <div class="dropdown-menu m-0 p-0">
  //                     <a class="dropdown-item p-3" href="#">
  //                       <div class="d-flex align-items-top">
  //                         <div class="h4">
  //                           <i class="ri-save-line"></i>
  //                         </div>
  //                         <div class="data ms-2">
  //                           <h6>Save Post</h6>
  //                           <p class="mb-0">
  //                             Add this to your saved items
  //                           </p>
  //                         </div>
  //                       </div>
  //                     </a>
  //                     <a class="dropdown-item p-3" href="#">
  //                       <div class="d-flex align-items-top">
  //                         <i class="ri-close-circle-line h4"></i>
  //                         <div class="data ms-2">
  //                           <h6>Hide Post</h6>
  //                           <p class="mb-0">
  //                             See fewer posts like this.
  //                           </p>
  //                         </div>
  //                       </div>
  //                     </a>
  //                     <a class="dropdown-item p-3" href="#">
  //                       <div class="d-flex align-items-top">
  //                         <i class="ri-user-unfollow-line h4"></i>
  //                         <div class="data ms-2">
  //                           <h6>Unfollow User</h6>
  //                           <p class="mb-0">
  //                             Stop seeing posts but stay friends.
  //                           </p>
  //                         </div>
  //                       </div>
  //                     </a>
  //                     <a class="dropdown-item p-3" href="#">
  //                       <div class="d-flex align-items-top">
  //                         <i class="ri-notification-line h4"></i>
  //                         <div class="data ms-2">
  //                           <h6>Notifications</h6>
  //                           <p class="mb-0">
  //                             Turn on notifications for this post
  //                           </p>
  //                         </div>
  //                       </div>
  //                     </a>
  //                   </div>
  //                 </div>
  //               </div>
  //             </div>
  //           </div>
  //         </div>
  //       </div>
  //       <div class="mt-3">
  //         <p>
  //           Lorem ipsum dolor sit amet, consectetur adipiscing elit.
  //           Morbi nulla dolor, ornare at commodo non, feugiat non
  //           nisi. Phasellus faucibus mollis pharetra. Proin blandit
  //           ac massa sed rhoncus
  //         </p>
  //       </div>
  //       <div class="user-post">
  //         <div class="d-grid grid-rows-2 grid-flow-col gap-3">
  //           <div class="row-span-2 row-span-md-1">
  //             <img src="../assets/images/page-img/p2.jpg" alt="post-image" class="img-fluid rounded w-100">
  //           </div>
  //           <div class="row-span-1">
  //             <img src="../assets/images/page-img/p1.jpg" alt="post-image" class="img-fluid rounded w-100">
  //           </div>
  //           <div class="row-span-1">
  //             <img src="../assets/images/page-img/p3.jpg" alt="post-image" class="img-fluid rounded w-100">
  //           </div>
  //         </div>
  //       </div>
  //       <div class="comment-area mt-3">
  //         <div class="d-flex justify-content-between align-items-center flex-wrap">
  //           <div class="like-block position-relative d-flex align-items-center">
  //             <div class="d-flex align-items-center">
  //               <div class="like-data">
  //                 <div class="dropdown">
  //                   <span class="dropdown-toggle" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false" role="button">
  //                     <img src="../assets/images/icon/01.png" class="img-fluid" alt="">
  //                   </span>
  //                   <div class="dropdown-menu py-2">
  //                     <a class="ms-2 me-2" href="#" data-bs-toggle="tooltip" data-bs-placement="top" title="" data-bs-original-title="Like" aria-label="Like"><img src="../assets/images/icon/01.png" class="img-fluid" alt=""></a>
  //                     <a class="me-2" href="#" data-bs-toggle="tooltip" data-bs-placement="top" title="" data-bs-original-title="Love" aria-label="Love"><img src="../assets/images/icon/02.png" class="img-fluid" alt=""></a>
  //                     <a class="me-2" href="#" data-bs-toggle="tooltip" data-bs-placement="top" title="" data-bs-original-title="Happy" aria-label="Happy"><img src="../assets/images/icon/03.png" class="img-fluid" alt=""></a>
  //                     <a class="me-2" href="#" data-bs-toggle="tooltip" data-bs-placement="top" title="" data-bs-original-title="HaHa" aria-label="HaHa"><img src="../assets/images/icon/04.png" class="img-fluid" alt=""></a>
  //                     <a class="me-2" href="#" data-bs-toggle="tooltip" data-bs-placement="top" title="" data-bs-original-title="Think" aria-label="Think"><img src="../assets/images/icon/05.png" class="img-fluid" alt=""></a>
  //                     <a class="me-2" href="#" data-bs-toggle="tooltip" data-bs-placement="top" title="" data-bs-original-title="Sade" aria-label="Sade"><img src="../assets/images/icon/06.png" class="img-fluid" alt=""></a>
  //                     <a class="me-2" href="#" data-bs-toggle="tooltip" data-bs-placement="top" title="" data-bs-original-title="Lovely" aria-label="Lovely"><img src="../assets/images/icon/07.png" class="img-fluid" alt=""></a>
  //                   </div>
  //                 </div>
  //               </div>
  //               <div class="total-like-block ms-2 me-3">
  //                 <div class="dropdown">
  //                   <span class="dropdown-toggle" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false" role="button">
  //                     140 Likes
  //                   </span>
  //                   <div class="dropdown-menu">
  //                     <a class="dropdown-item" href="#">Max Emum</a>
  //                     <a class="dropdown-item" href="#">Bill Yerds</a>
  //                     <a class="dropdown-item" href="#">Hap E. Birthday</a>
  //                     <a class="dropdown-item" href="#">Tara Misu</a>
  //                     <a class="dropdown-item" href="#">Midge Itz</a>
  //                     <a class="dropdown-item" href="#">Sal Vidge</a>
  //                     <a class="dropdown-item" href="#">Other</a>
  //                   </div>
  //                 </div>
  //               </div>
  //             </div>
  //             <div class="total-comment-block">
  //               <div class="dropdown">
  //                 <span class="dropdown-toggle" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false" role="button">
  //                   20 Comment
  //                 </span>
  //                 <div class="dropdown-menu">
  //                   <a class="dropdown-item" href="#">Max Emum</a>
  //                   <a class="dropdown-item" href="#">Bill Yerds</a>
  //                   <a class="dropdown-item" href="#">Hap E. Birthday</a>
  //                   <a class="dropdown-item" href="#">Tara Misu</a>
  //                   <a class="dropdown-item" href="#">Midge Itz</a>
  //                   <a class="dropdown-item" href="#">Sal Vidge</a>
  //                   <a class="dropdown-item" href="#">Other</a>
  //                 </div>
  //               </div>
  //             </div>
  //           </div>
  //           <div class="share-block d-flex align-items-center feather-icon mt-2 mt-md-0">
  //             <a href="javascript:void();" data-bs-toggle="offcanvas" data-bs-target="#share-btn" aria-controls="share-btn"><i class="ri-share-line"></i>
  //               <span class="ms-1">99 Share</span></a>
  //           </div>
  //         </div>
  //         <hr>
  //         <ul class="post-comments list-inline p-0 m-0">
  //           <li class="mb-2">
  //             <div class="d-flex">
  //               <div class="user-img">
  //                 <img src="../assets/images/user/02.jpg" alt="userimg" class="avatar-35 rounded-circle img-fluid">
  //               </div>
  //               <div class="comment-data-block ms-3">
  //                 <h6>Monty Carlo</h6>
  //                 <p class="mb-0">Lorem ipsum dolor sit amet</p>
  //                 <div class="d-flex flex-wrap align-items-center comment-activity">
  //                   <a href="javascript:void();">like</a>
  //                   <a href="javascript:void();">reply</a>
  //                   <a href="javascript:void();">translate</a>
  //                   <span> 5 min </span>
  //                 </div>
  //               </div>
  //             </div>
  //           </li>
  //           <li>
  //             <div class="d-flex">
  //               <div class="user-img">
  //                 <img src="../assets/images/user/03.jpg" alt="userimg" class="avatar-35 rounded-circle img-fluid">
  //               </div>
  //               <div class="comment-data-block ms-3">
  //                 <h6>Paul Molive</h6>
  //                 <p class="mb-0">Lorem ipsum dolor sit amet</p>
  //                 <div class="d-flex flex-wrap align-items-center comment-activity">
  //                   <a href="javascript:void();">like</a>
  //                   <a href="javascript:void();">reply</a>
  //                   <a href="javascript:void();">translate</a>
  //                   <span> 5 min </span>
  //                 </div>
  //               </div>
  //             </div>
  //           </li>
  //         </ul>
  //         <form class="comment-text d-flex align-items-center mt-3" action="javascript:void(0);">
  //           <input type="text" class="form-control rounded" placeholder="Enter Your Comment">
  //           <div class="comment-attagement d-flex">
  //             <a href="javascript:void();"><i class="ri-link me-3"></i></a>
  //             <a href="javascript:void();"><i class="ri-user-smile-line me-3"></i></a>
  //             <a href="javascript:void();"><i class="ri-camera-line me-3"></i></a>
  //           </div>
  //         </form>
  //       </div>
  //     </div>
  //   </div>
  //   </div>`;
  //   containerPost.insertAdjacentHTML("beforeend", html);
  // });
}

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

function formatDate(date) {
  var hours = date.getHours();
  var minutes = date.getMinutes();
  var ampm = hours >= 12 ? "pm" : "am";
  hours = hours % 12;
  hours = hours ? hours : 12; // the hour '0' should be '12'
  minutes = minutes < 10 ? "0" + minutes : minutes;
  var strTime = hours + ":" + minutes + " " + ampm;
  return (
    date.getDate() +
    "/" +
    (date.getMonth() + 1) +
    "/" +
    date.getFullYear() +
    "  " +
    strTime
  );
}
