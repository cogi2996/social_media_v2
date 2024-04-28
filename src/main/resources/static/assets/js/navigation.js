const notificationElement = document.getElementById("notification-drop");
const cardBodyElement =
  notificationElement.nextElementSibling.querySelector(".card-body");
let isFetchingNotifications = false;
document
  .getElementById("notification-drop")
  .addEventListener("click", function (e) {
    if (isFetchingNotifications) return;
    const pageSize = 5;
    const pageNum = Math.ceil(cardBodyElement.childElementCount / pageSize);
    console.log(cardBodyElement.childElementCount);
    getNotifications(pageNum, pageSize);
    isFetchingNotifications = true;
  });

async function getNotifications(pageNum, pageSize = 5) {
  try {
    const { data, status } = await axios.get(
      `/api/v1/users/notifications?pageNum=${pageNum}&pageSize=${pageSize}`
    );
    if (status !== 200) {
      throw new Error("Error");
    }
    [...data].forEach((item) => {
      console.log(item.notification.type);

      if (item.notification.type === "LIKE") renderNotificationsTypeLike(item);
    });
    isFetchingNotifications = false;
  } catch (error) {
    console.error(error);
  }
}

// render notifications from list notifications
function renderNotificationsTypeLike(data) {
  let { notification: noti, userLiked, post } = data; // Changed 'notification' to 'notiData'
  console.log("noti here");
  console.log(noti);

  const avatar =
    userLiked.avatar === null
      ? `/assets/images/user/defaul_avatar.jpg`
      : userLiked.avatar;

  const html = `
       <a href="/${curUserId}/post/${post.postId}" class="iq-sub-card">
       <div class="d-flex align-items-center">
         <div class="">
           <img class="avatar-40 rounded" src="${avatar}" alt="">
         </div>
         <div class="ms-3 w-100">
           <h6 class="mb-0">${userLiked.firstName} đã like bài viết của bạn</h6>
           <div class="d-flex justify-content-between align-items-center">
             <p class="mb-0">${post.postText.substring(0, 10)}...</p>
           </div>
         </div>
       </div>
     </a>   
      `;

  cardBodyElement.insertAdjacentHTML("beforeend", html);
}

// chức năng tìm kiếm
document
  .querySelector(".iq-top-navbar input")
  .addEventListener("keypress", function (e) {
    if (e.key === "Enter") {
      e.preventDefault();
      e.currentTarget.value.trim() != "" &&
        (window.location.href = `/search/top?name=${e.currentTarget.value.trim()}`);
    }
  });

// load infor user in navigation
function loadInforUser() {
  axios
    .get(`/api/v1/users/current`)
    .then(function (response) {
      const { data, status } = response;
      if (status === 200) {
         console.log(data);
        const avatar =
          data.avatar === null
            ? `/assets/images/user/defaul_avatar.jpg`
            : data.avatar;
        document.getElementById("top-header-avatar").src = avatar;
        document.querySelector(
          ".caption h6"
        ).innerText = `${data.lastName} ${data.midName} ${data.firstName} `;
        document.querySelector(
          "#dropdown-header-title"
        ).innerText = `Xin chào, ${data.firstName} `;
        document.getElementById(
          "dropdown-link-profile"
        ).href = `/profile?id=${data.userId}`;
      }
    })
    .catch(function (error) {
      console.log(error);
    })
    .finally(function () {});
}

loadInforUser();
loadRecentRequest();

// chức năng thể hiện  4 người gởi follow gần nhất
function loadRecentRequest() {
  axios
    .get(`/api/v1/users/pendingFollow?page=0&pageSize=4`)
    .then(function (response) {
      const { data, status } = response;
      if (status === 200) {
        // console.log(data);
        document.getElementById("total-pending-request").innerHTML =
          data.length;
        const endElement = `
        <div class="text-center">
          <a href="/follows" class="btn text-primary">Xem thêm</a>
        </div>
        `;
        data.push(endElement);
        // console.log(data);

        data.forEach((friend, index) => {
          renderRecentRequest(friend, index === data.length - 1);
        });
      }
    })
    .catch(function (error) {
      console.log(error);
    })
    .finally(function () {});
}

function renderRecentRequest(friend, isEnd = false) {
  const container = document.querySelector("#top-friend-request");
  if (isEnd) {
    container.insertAdjacentHTML("beforeend", friend);
    return;
  }
  const avatar =
    friend.avatar === null
      ? `/assets/images/user/defaul_avatar.jpg`
      : friend.avatar;

  console.log(container);
  const html = `
      <div class="iq-friend-request" data-user-id="${friend.userId}">
        <div class="iq-sub-card iq-sub-card-big d-flex align-items-center justify-content-between">
            <div class="d-flex align-items-center">
                <img class="avatar-40 rounded" src="${avatar}" alt="">
                <div class="ms-3">
                    <h6 class="mb-0">${friend.lastName} ${friend.midName} ${friend.firstName}</h6>
                    <p class="mb-0">${friend.department}</p>
                </div>
            </div>
            <div class="d-flex align-items-center" >
                <a href="#" class="me-3 btn btn-primary rounded">Chấp nhận</a>
                <a href="#" class="me-3 btn btn-secondary rounded">Xoá</a>
            </div>
        </div>
    </div>
  `;
  container.insertAdjacentHTML("beforeend", html);
}

document
  .querySelector("#top-friend-request")
  .addEventListener("click", function (e) {
    // thực hiện accept follow
    if (e.target.closest(".btn-primary")) {
      // e.preventDefault();
      console.log(e.target.closest(".btn-primary"));

      const sourceId = e.target
        .closest(".btn-primary")
        .closest(".iq-friend-request").dataset.userId;
      axios
        .patch(`/api/v1/users/${sourceId}/followers`)
        .then(function (response) {
          console.log(response);
          e.target.closest("div").innerHTML = "<p>Đã chấp nhận</p>";
        })
        .catch(function (error) {
          console.log(error);
        })
        .finally(function () {});
    } else if (e.target.closest(".btn-secondary")) {
      const sourceId = e.target.closest(".iq-friend-request").dataset.userId;
      axios
        .delete(`/api/v1/users/${sourceId}/follows`)
        .then(function (response) {
          console.log(response);
          e.target.closest("div").innerHTML = "<p>Đã xoá theo dõi</p>";
        })
        .catch(function (error) {
          console.log(error);
        });
    }
  });
