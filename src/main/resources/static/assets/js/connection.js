"use strict";
const $ = document.querySelector.bind(document);
const $$ = document.querySelectorAll.bind(document);
const curConnectUserId = $("#friends").dataset.userId;
const curUserId = jwt_decode(Cookies.get("access_token")).userId;
console.log(curConnectUserId + " &&" + curUserId);

//when click on followings tab
let firstVisitFollowing = true;
let firstVisitFollower = true;
let isFetchingFollowing = false;
let isFetchingFollower = false;

const followingsLink = $(".nav-link[data-bs-target='#followings']");
const followersLink = $(".nav-link[data-bs-target='#followers']");
const followingContainer = $("#following-container .row");
const followerContainer = $("#follower-container .row");
const loader = document.querySelector('.container div img[alt="loader"]');
const pageSize = 6;

// document
//   .querySelector(".nav-link[data-bs-target='#followings']")
//   .addEventListener("click", () => {
//     if (firstVisitFollowing) {
//       renderFollow();
//       return;
//     }
//   });

// when click on followers tab
document
  .querySelector(".nav-link[data-bs-target='#followers']")
  .addEventListener("click", () => {
    console.log("hello 2");
  });

// init follow
getFollowListHandle(true);
getFollowListHandle(false);

// follow render handle
window.addEventListener("scroll", () => {
  if (
    window.scrollY + window.innerHeight + 1 >=
    document.documentElement.scrollHeight
  ) {
    const isFollowingsLinkClicked = followingsLink.classList.contains("active");

    getFollowListHandle(isFollowingsLinkClicked);
  }
});

$(".friend-list-tab ul ").addEventListener("click", (e) => {
  if (e.target.closest(".nav-link[data-bs-target='#followings']")) {
    // hiden các tab khác
    followingContainer
      .closest("#following-container")
      .classList.add("active", "show");
    followerContainer
      .closest("#follower-container")
      .classList.remove("active", "show");
  } else {
    // hiden các tab khác
    followerContainer
      .closest("#follower-container")
      .classList.add("active", "show");
    followingContainer
      .closest("#following-container")
      .classList.remove("active", "show");
  }
});

// lấy ra danh sách những người đang theo dõi và render ra màn hình
function getFollowListHandle(isFollowingsLinkClicked) {
  if (isFollowingsLinkClicked) {
    const countCurUserFollowing = $$(
      "#following-container .iq-friendlist-block"
    ).length;
    const pageNum = Math.ceil(countCurUserFollowing / pageSize);
    console.log(`pageNum: ${pageNum}`);
    loader.classList.remove("d-none");
    if (isFetchingFollowing) return;
    axios
      .get(
        `/api/v1/users/${curConnectUserId}
      /followings?pageNum=${pageNum}&pageSize=${pageSize}&curentUserId=${curUserId}`
      )
      .then(function (response) {
        if (response.status === 200) {
          const { data, message, status } = response;
          console.log(data, message, status);
          renderFollowCard(data, isFollowingsLinkClicked);
          loader.classList.add("d-none");
        }
      })
      .catch(function (error) {
        console.log(error);
      })
      .finally(function () {
        isFetchingFollowing = false;
      });
    isFetchingFollowing = true;
  } else {
    const countCurFollowers = $$(
      "#follower-container .iq-friendlist-block"
    ).length;
    const pageNum = Math.ceil(countCurFollowers / pageSize);
    console.log(`pageNum: ${pageNum}`);
    loader.classList.remove("d-none");
    if (isFetchingFollower) return;
    axios
      .get(
        `/api/v1/users/${curConnectUserId}
      /followers?pageNum=${pageNum}&pageSize=${pageSize}&curentUserId=${curUserId}`
      )
      .then(function (response) {
        if (response.status === 200) {
          const { data, message, status } = response;
          console.log(data, message, status);
          renderFollowCard(data, isFollowingsLinkClicked);
          loader.classList.add("d-none");
        }
      })
      .catch(function (error) {
        console.log(error);
      })
      .finally(function () {
        isFetchingFollower = false;
      });
    isFetchingFollower = true;
  }
}

// render các card người dùng vào container tương ứng từ danh sách user
function renderFollowCard(listUser, isFollowingsLinkClicked) {
  const container = isFollowingsLinkClicked
    ? followingContainer
    : followerContainer;

  listUser.forEach((user) => {
    const typeButton = user.isFollowed ? "secondary" : "primary";
    const textButton = user.isFollowed ? "Đang theo dõi 😊" : "Theo dõi";
    const isDropdownMenu = !user.isFollowed ? "d-none" : "";
    const avatar =
      user.avatar === null
        ? `/assets/images/user/defaul_avatar.jpg`
        : user.avatar;
    const isSpan = user.isFollowed ? "span" : "a";
    const isHref = user.isFollowed
      ? ""
      : `href="/follows/requestFollow/${user.userId}"`;
    const html = `
      <div class="col-md-6 col-lg-6 mb-3">
        <div class="iq-friendlist-block">
          <div class="d-flex align-items-center justify-content-between">
            <div class="d-flex align-items-center">
              <a href="#">
              <img src="${avatar}" style="width: 130px; height: 130px; object-fit: cover;" alt="userimg">
              </a>
              <div class="friend-info ms-3">
              <a href="/profile?id=${user.userId}">
                <h5>${user.lastName} ${user.midName} ${user.firstName}</h5>
              </a>
                <p class="mb-0">${user.department}</p>
              </div>
            </div>
            <div class="card-header-toolbar d-flex align-items-center">
              <form class="dropdown">
                <${isSpan} ${isHref} class="dropdown-toggle btn btn-${typeButton} me-2" id="dropdownMenuButton01" data-bs-toggle="dropdown" aria-expanded="true" role="button">
                  ${textButton}
                </${isSpan}>
                <div class="dropdown-menu dropdown-menu-right ${isDropdownMenu}" aria-labelledby="dropdownMenuButton01">
                  <a class="dropdown-item" href="#">Báo cáo</a>
                  <a class="dropdown-item" href="/follows/deleteFollow/${user.userId}">Unfollow</a>
                  <a class="dropdown-item" href="#">Block</a>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>`;
    container.insertAdjacentHTML("beforeend", html);
  });
}

// click follow button khi chưa following
//1 đỗi sang đang theo dõi

// click follow button khi đang following
