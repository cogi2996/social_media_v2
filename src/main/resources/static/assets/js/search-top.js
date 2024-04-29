"use strict";
let isDone = false;
document
  .querySelector("#viewMoreSearch")
  .addEventListener("click", function (e) {
    console.log("here");
    e.preventDefault();
    const keyword = document.querySelector(".card-title").dataset.keyword;
    !isDone && getMoreResult(keyword, e.currentTarget.parentElement);
  });
// hello
function getMoreResult(name, container) {
  const pageSize = 3;
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
  const isPennding = user.isFollowed === 2 ? true : false;
  const isNotFollowing = user.isFollowed === 0 ? true : false;
  console.log(isNotFollowing, isPennding, isFollowing);

  const html = `    
                            <li class="d-flex align-items-center justify-content-between flex-wrap" data-user-id ="${
                              user.userId
                            }">
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
                                <div class="d-flex align-items-center mt-2 mt-md-0" id="container-btn-follow" >
                                    <button type="button" id="btn-follow" class="btn btn-primary mt-1  ${
                                      isNotFollowing ? "" : "d-none"
                                    }" style="width: 150px; height: auto">
                                        theo dõi
                                    </button>
                                    <button type="button" id="btn-unfollow" class="btn btn-secondary mt-1   ${
                                      isPennding ? "" : "d-none"
                                    }" style="width: 150px; height: auto">
                                        Đã gửi yêu cầu 
                                    </button>
                                    <form class="dropdown 
                                    ${isFollowing ? "" : "d-none"}">
                                            <span class="dropdown-toggle btn btn-secondary " id="dropdownMenuButton01" data-bs-toggle="dropdown"
                                                  aria-expanded="false" role="button"
                                                  style="width: 150px; height: auto"
                                            >
                                              Đang theo dõi
                                            </span>
                                            <div class="dropdown-menu dropdown-menu-right"
                                                aria-labelledby="dropdownMenuButton01" style="">
    <!--                                            <a class="dropdown-item" href="#">Báo cáo</a>-->
                                                <a class="dropdown-item" id="unfollowLink" href="#">Huỷ theo dõi</a>
                                            </div>
                                    </form>
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
      followButton.classList.toggle("d-none");
      unfollowButton.classList.toggle("d-none");
      followHandle(targetId);
    } else if (
      event.target.closest("#btn-unfollow") ||
      event.target.closest("#unfollowLink")
    ) {
      event.preventDefault();
      const unfollowButton =
        event.target.closest("#btn-unfollow") ||
        event.target.closest("#unfollowLink");
      const followButton = unfollowButton
        .closest("#container-btn-follow")
        .querySelector("#btn-follow");
      const targetId = followButton.closest("li").dataset.userId;
      followButton.classList.toggle("d-none");
      if (event.target.closest("#unfollowLink"))
        unfollowButton.closest("form").classList.toggle("d-none");
      else unfollowButton.classList.toggle("d-none");
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
