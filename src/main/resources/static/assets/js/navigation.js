const notificationElement = document.getElementById("notification-drop");
const cardBodyElement =
  notificationElement.nextElementSibling.querySelector(".card-body");
const curUserId = jwt_decode(Cookies.get("access_token")).userId;
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
      `/api/v1/users/${curUserId}/notifications?pageNum=${pageNum}&pageSize=${pageSize}`
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
  let { notiData, userLiked, post } = data; // Changed 'notification' to 'notiData'
  console.log(userLiked);

  const avatar =
    userLiked.avatar === null
      ? `/assets/images/user/defaul_avatar.jpg`
      : userLiked.avatar;

  const html = `
       <a href="#" class="iq-sub-card">
       <div class="d-flex align-items-center">
         <div class="">
           <img class="avatar-40 rounded" src="${avatar}" alt="">
         </div>
         <div class="ms-3 w-100">
           <h6 class="mb-0">${userLiked.firstName} đã like bài viết của bạn</h6>
           <div class="d-flex justify-content-between align-items-center">
             <p class="mb-0">${post.postText.substring(0, 10)}...</p>
             <small class="float-right font-size-12">5 days ago</small>
           </div>
         </div>
       </div>
     </a>   
      `;

  cardBodyElement.insertAdjacentHTML("beforeend", html);
}
