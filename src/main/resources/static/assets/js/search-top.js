"use strict";
let isDone = false;
document
  .querySelector("#viewMoreSearch")
  .addEventListener("click", function (e) {
    console.log("here");
    e.preventDefault();
    !isDone && getMoreResult("tuan", e.currentTarget.parentElement);
  });

function getMoreResult(name, container) {
  const pageSize = 5;
  const pageNum = Math.ceil(
    (container.closest("ul").childElementCount - 1) / pageSize
  );
  console.log(pageNum);
  axios
    .get(
      `/api/v1/users/search?name=${name}&pageNum=${pageNum}&pageSize=${pageSize}`
    )
    .then(function ({ data, status }) {
      if (status === 200) {
        console.log(data);
        data.forEach((user) => {
          console.log(user);
          renderUserResult(user, container);
        });
      }
    })
    .catch(function (error) {
      console.log(error);
    })
    .finally(function () {});
}

function renderUserResult(user, container) {
  const isFollowing = user.isFollowed === 1 ? true : false;
  const isPennding = user.isFollowed === 0 ? true : false;
  console.log("isFollowing" + " " + isFollowing);
  console.log("isPennding" + " " + isPennding);

  const html = `    
                            <li class="d-flex align-items-center justify-content-between flex-wrap">
                                <div class="user-img img-fluid flex-shrink-0">
                                    <img src="${
                                      user.avatar === null
                                        ? `/assets/images/user/defaul_avatar.jpg`
                                        : user.avatar
                                    }" class="rounded-circle avatar-40">
                                    
                                </div>
                                <div class="flex-grow-1 ms-3">
                                    <h6>${user.lastName} ${user.midName} ${
    user.firstName
  }</h6>
                                    <p class="mb-0">40 friends</p>
                                </div>
                                <div class="d-flex align-items-center mt-2 mt-md-0">
                                    <button type="button" id="btn-follow" class="btn btn-primary mt-1 badge ${
                                      isFollowing ? "" : "d-none"
                                    }" style="width: 110px; height: auto">
                                        theo dõi
                                    </button>
                                    <button type="button" id="btn-unfollow" class="btn btn-secondary mt-1 badge badge ${
                                      isPennding ? "" : "d-none"
                                    }" style="width: 110px; height: auto">
                                        Đã gửi yêu cầu 😊
                                    </button>
                                </div>
                                
                            </li>
    `;
  container.insertAdjacentHTML("beforebegin", html);
}

// render icon theo state follow của user
// document.querySelectorAll(".card-body ul li[data-user-id]");

// follow hander
document
  .querySelector(".card-body ul")
  ?.addEventListener("click", function (event) {
    if (event.target.closest("#btn-follow")) {
      const followButton = event.target.closest("#btn-follow");
      const unfollowButton = followButton.nextElementSibling;
      const targetId = followButton.closest("li").dataset.userId;
      const sourceId = jwt_decode(Cookies.get("access_token")).userId;
      followButton.classList.toggle("d-none");
      unfollowButton.classList.toggle("d-none");
      console.log(sourceId + " " + targetId);
      followHandle(sourceId, targetId);
    } else if (event.target.closest("#btn-unfollow")) {
      const unfollowButton = event.target.closest("#btn-unfollow");
      const followButton = unfollowButton.previousElementSibling;
      const targetId = followButton.closest("li").dataset.userId;
      const sourceId = jwt_decode(Cookies.get("access_token")).userId;
      followButton.classList.toggle("d-none");
      unfollowButton.classList.toggle("d-none");
      console.log(sourceId + " " + targetId);
      unfollowHandle(sourceId, targetId);
    }
  });

function followHandle(sourceId = null, targetId = null) {
  axios
    .post(`/api/v1/users/${sourceId}/follows/${targetId}`)
    .then(function ({ status }) {
      console.log(status);
    })
    .catch(function (error) {
      console.log(error);
    });
}
function unfollowHandle(sourceId = null, targetId = null) {
  axios
    .delete(`/api/v1/users/${sourceId}/follows/${targetId}`)
    .then(function ({ status }) {
      console.log(status);
    })
    .catch(function (error) {
      console.log(error);
    });
}

// render state follow
// function renderSateFollow() {
//   const isFollowing = user.isFollowed === 1 ? true : false;
//   const isPennding = user.isFollowed === 0 ? true : false;
//   console.log("isFollowing" + " " + isFollowing);
//   console.log("isPennding" + " " + isPennding);
//   [...document.querySelectorAll(".card-body ul li[data-user-id]")].forEach(
//     (userElement) => {

//     }
//   );
// }
