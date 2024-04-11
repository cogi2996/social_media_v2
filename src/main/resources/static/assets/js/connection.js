"use strict";
const $ = document.querySelector.bind(document);
const $$ = document.querySelectorAll.bind(document);
const curConnectUserId = $("#friends").dataset.userId;
const curUserId = jwt_decode(Cookies.get("access_token")).userId;

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
    const textButton =
      user.isFollowed === 1
        ? "Đang theo dõi 😊"
        : user.isFollowed === 2
        ? "Đã gửi yêu cầu"
        : "Theo dõi";
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
      <div class="col-md-6 col-lg-6 mb-3" data-user-id = ${user.userId}>
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
                <span class="dropdown-toggle btn btn-${typeButton} me-2" id="dropdownMenuButton01" data-bs-toggle="dropdown" aria-expanded="true" role="button">
                  ${textButton}
                </span>
                <div class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownMenuButton01">
                  <a class="dropdown-item" href="#">Báo cáo</a>
                  <a class="dropdown-item" href="/follows/deleteFollow/${user.userId}">Unfollow</a>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>`;
    container.insertAdjacentHTML("beforeend", html);
  });
}

// click follow button khi đang following
followingContainer.addEventListener("click", (e) => {
  if (
    e.target.closest("#dropdownMenuButton01") &&
    e.target.closest("#dropdownMenuButton01").classList.contains("btn-primary")
  ) {
    e.preventDefault();
    e.target.closest("#dropdownMenuButton01").classList.remove("btn-primary");
    e.target.closest("#dropdownMenuButton01").classList.add("btn-secondary");
    e.target.closest("#dropdownMenuButton01").textContent = "Đã gửi yêu cầu";
  }
});

followerContainer.addEventListener("click", (e) => {
  if (
    e.target.closest("#dropdownMenuButton01") &&
    e.target.closest("#dropdownMenuButton01").classList.contains("btn-primary")
  ) {
    e.target.closest("#dropdownMenuButton01").classList.remove("btn-primary");
    e.target.closest("#dropdownMenuButton01").classList.add("btn-secondary");
    e.target.closest("#dropdownMenuButton01").textContent = "Đã gửi yêu cầu";
    const thisUserId = e.target.closest(".iq-friendlist-block").parentNode
      .dataset.userId;
    console.log(thisUserId);

    axios
      .post(`/api/v1/users/${curConnectUserId}/follows/${thisUserId}`)
      .then(function (response) {
        console.log(response);
      })
      .catch(function (error) {
        console.log(error);
      })
      .finally(function () {});
  }
});
