import { app } from "./firebase/firebase.js";

import {
  ref as dbRef,
  set,
  onValue,
  getDatabase,
  push,
} from "https://www.gstatic.com/firebasejs/10.9.0/firebase-database.js";

const db = getDatabase(app);
console.log(db);

async function uploadComment(postID, content) {
  if (content == "") return;
  const { data: curUserInfo } = await axios.get("/api/v1/users/current");
  console.log("here: " + curUserInfo.userId);

  const userId = curUserInfo.userId;
  const fullName = `${curUserInfo.lastName}  ${curUserInfo.midName}  ${curUserInfo.firstName} `;
  const createAt = Date.now();
  //   const avatar = currentUser.photoURL;
  const newComment = {
    userId: userId,
    fullName: fullName,
    content: content,
    createAt: createAt,
  };
  let commentRef = push(dbRef(db, `posts/${postID}/comments`));
  // upload lên firebase
  set(commentRef, newComment)
    .then(() => {})
    .catch((error) => {
      console.error("Error writing comment: ", error);
    });
}
// uploadComment(3, "content");

// Lấy tất cả các thẻ input trong các bài đăng

// Thêm sự kiện 'keypress' cho mỗi thẻ input

document
  .getElementById("container__post")
  .addEventListener("click", function (e) {
    if (e.target.closest(".input-comment")) {
      console.log("da click vao input-comment");
      e.target.closest(".input-comment").addEventListener("keypress", (e) => {
        if (e.key === "Enter") {
          e.preventDefault();
          const postID = e.target.closest(".col-sm-12").dataset.postId;
          const content = e.target.value;
          uploadComment(postID, content);
          e.target.value = "";
        }
      });
    }
  });

// if (e.target.closest(".post__feedback-comment")) {
//   let commentRef = dbRef(
//     db,
//     `posts/${currentObserverPost.dataset.postId}/comments`
//   );
//   onValue(commentRef, (snapshot) => {
//     const comments = snapshot.val();
//     for (const commentKey in comments) {
//       if (listCommentOsereverd.has(commentKey)) continue;
//       const comment = comments[commentKey];
//     }
//   });
// }

function renderRealTimeComment(postElement) {
  let commentRef = dbRef(db, `posts/${postElement.dataset.postId}/comments`);
  onValue(commentRef, (snapshot) => {
    const comments = snapshot.val();
    console.log(comments);
    //list các comment đã có
    const renderedCommentsId = [
      ...postElement.querySelectorAll("li[data-comment-id]"),
    ].map((comment) => comment.dataset.commentId);
    for (const commentKey in comments) {
      if (renderedCommentsId.includes(commentKey)) continue;
      console.log(comments[commentKey]);
      const comment = comments[commentKey];
      const html = `
        <li class="mb-2" data-comment-id=${commentKey}>
        <div class="d-flex">
          <div class="comment-data-block ms-3">
            <h6>${comment.fullName}</h6>
            <p class="mb-0">${comment.content}</p>
          </div>
        </div>
      </li>
        `;
      postElement
        .querySelector(".post-comments")
        .insertAdjacentHTML("beforeend", html);
    }
  });
}

export { uploadComment, renderRealTimeComment };
