import { app } from "../firebase/firebase.js";
import {
  getStorage,
  ref,
  uploadBytes,
  getDownloadURL,
  uploadBytesResumable,
} from "https://www.gstatic.com/firebasejs/10.9.0/firebase-storage.js";
import { uploadImage } from "../uploadFileService.js";
import { uploadComment, renderRealTimeComment } from "../commentService.js";
console.log(Cookies);
const btnImage = document.getElementById("btn-image");
const inputImg = btnImage.querySelector("#image");
const btnSubmit = document.querySelector('button[type="submit"]');
const createPost = document.getElementById("post-modal");
const postContainer = document.getElementById("container__post");
const $ = document.querySelector.bind(document);
const $$ = document.querySelectorAll.bind(document);
const storage = getStorage(app);

getListNewPost(0, 3);
btnImage.addEventListener("click", () => {
  console.log("clickedimg");

  inputImg.click();
});

btnSubmit.addEventListener("click", (e) => {
  e.preventDefault();
  handleFormSubmit();
});

async function handleFormSubmit(token = null) {
  const url = inputImg.files[0] ? await uploadImage(inputImg.files[0]) : null;
  let text = createPost.querySelector('input[type="text"]').value;
  // post api
  axios
    .post("http://localhost:8080/api/v1/posts", {
      postText: text,
      postImage: url,
    })
    .then(function (response) {
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
  if (
    window.scrollY + window.innerHeight + 1 >=
    document.documentElement.scrollHeight
  ) {
    const totalCurNumPost = postContainer.querySelectorAll(
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
      console.log(data);
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
  const liked = post.liked;
  // bg-soft-primary
  //text-primary
  const postImage = post.postImage
    ? `
  <a href="javascript:void();"><img src="${post.postImage}" alt="post-image" class="img-fluid rounded w-100"></a>
  `
    : "";
  const textColor = liked ? "text-primary" : "";
  const isDisplayLike = !liked ? "d-none" : "";
  const avatar =
    post.userDTO.avatar === null
      ? `/assets/images/user/defaul_avatar.jpg`
      : post.userDTO.avatar;
  let html = `<div class="col-sm-12" data-post-id = ${
    post.postId
  } data-user-post-id = ${post.userDTO.userId}>
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
                    ${post.userDTO.lastName} ${post.userDTO.midName} ${
    post.userDTO.firstName
  }
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
        ${postImage}
      </div>
      <div class="comment-area mt-3">
        <div class="d-flex justify-content-between align-items-center flex-wrap">
          <div class="like-block position-relative d-flex align-items-center ">
          <div class="d-flex align-items-center btn-like">
          <div class="like-data">
              <span aria-haspopup="true" aria-expanded="false" role="button" class = "${isDisplayLike}">
                <img src="../assets/images/icon/01.png" class="img-fluid" alt="">
              </span>
              
          </div>
          <div class="total-like-block ms-2 me-3">
              <span role="button" class="${textColor}">
                ${post.countLike === 0 ? "Thích" : `${post.countLike} Thích`} 
            </span></div>
          </div>
            <div class="total-comment-block">
              <div class="dropdown">
                <span role="button">
                  Bình luận
                </span>
              </div>
            </div>
          </div>
        </div>
        <hr>
        <ul class="post-comments list-inline p-0 m-0" style="max-height: 300px; overflow-y: auto;">
        </ul>
        <form class="comment-text d-flex align-items-center mt-3" >
          <input type="text" class="input-comment form-control rounded" placeholder="Nhập bình luận của bạn">
        </form>
      </div>
    </div>
  </div>
  </div>`;
  postContainer.insertAdjacentHTML("beforeend", html);
  const postElement = document.querySelector(
    `.col-sm-12[data-post-id='${post.postId}']`
  );
  renderRealTimeComment(postElement);
}

// observer like event
postContainer.addEventListener("click", function (event) {
  // Check if the clicked element is a like button
  var likeButton = event.target.closest(".btn-like");
  if (likeButton) {
    // Handle the like button click
    handleLikeButtonClick(likeButton);
  }
});
function handleLikeButtonClick(button) {
  const likeIcon = button.querySelector(".like-data span");
  const likeText = button.querySelector(".total-like-block span");
  // tongle like icon & like text
  likeIcon.classList.toggle("d-none");
  likeText.classList.toggle("text-primary");

  const postId = button.closest(".col-sm-12").dataset.postId;

  // check if like or unlike
  const likeHandlerApi = likeIcon.classList.contains("d-none");
  if (!likeHandlerApi) {
    axios
      .post(`/api/v1/users/likeList/posts/${postId}`)
      .then(function ({ data }) {
        likeText.innerText = data === 0 ? "Thích" : `${data} Thích`;
      })
      .catch(function (error) {
        console.log(error);
      });
  } else {
    axios
      .delete(`/api/v1/users/likeList/posts/${postId}`)
      .then(function ({ data }) {
        // handle success
        likeText.innerText = data === 0 ? "Thích" : `${data} Thích`;
      })
      //   console.log(`here: ` + data);
      // })
      .catch(function (error) {
        console.log(error);
      });
  }
}

// tạo một component riêng [refactor]
// function uploadImage(file) {
//   return new Promise((resolve, reject) => {
//     const metadata = {
//       contentType: file.type,
//     };
//     const fileName = Date.now();
//     console.log(fileName);

//     const storageRef = ref(storage, `images/${fileName}`);
//     const uploadTask = uploadBytesResumable(storageRef, file, metadata);

//     uploadTask.on(
//       "onStateChanged",
//       (snapshot) => {
//         // Get task progress, including the number of bytes uploaded and the total number of bytes to be uploaded
//         const progress =
//           (snapshot.bytesTransferred / snapshot.totalBytes) * 100;
//         console.log("Upload is " + progress + "% done");
//         switch (snapshot.state) {
//           case "paused":
//             console.log("Upload is paused");
//             break;
//           case "running":
//             console.log("Upload is running");
//             break;
//         }
//       },
//       (error) => {
//         reject(`Có lỗi ở upload ảnh lên firebase: ` + error);
//       },
//       () => {
//         // Upload completed successfully, now we can get the download URL
//         getDownloadURL(uploadTask.snapshot.ref).then((downloadURL) => {
//           console.log("File available at", downloadURL);
//           resolve(downloadURL);
//         });
//       }
//     );
//   });
// }

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

// function follow
$("#follow-container")?.addEventListener("click", function (event) {
  if (event.target.closest("#btn-follow")) {
    const followButton = event.target.closest("#btn-follow");
    const unfollowButton = followButton.nextElementSibling;
    const targetId = followButton.closest("li").dataset.userId;
    followButton.classList.toggle("d-none");
    unfollowButton.classList.toggle("d-none");
    followHandle(targetId);
  } else if (event.target.closest("#btn-unfollow")) {
    const unfollowButton = event.target.closest("#btn-unfollow");
    const followButton = unfollowButton.previousElementSibling;
    const targetId = followButton.closest("li").dataset.userId;
    followButton.classList.toggle("d-none");
    unfollowButton.classList.toggle("d-none");
    unfollowHandle(targetId);
  }
});

function followHandle(targetId = null) {
  axios
    .post(`/api/v1/users/follows/${targetId}`)
    .then(function ({ status }) {
      console.log(status);
    })
    .catch(function (error) {
      console.log(error);
    });
}
function unfollowHandle(targetId = null) {
  axios
    .delete(`/api/v1/users/follows/${targetId}`)
    .then(function ({ status }) {
      console.log(status);
    })
    .catch(function (error) {
      console.log(error);
    });
}
