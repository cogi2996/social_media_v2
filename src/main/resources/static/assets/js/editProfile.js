import { uploadImage } from "./uploadFileService.js";
const $ = document.querySelector.bind(document);
const $$ = document.querySelectorAll.bind(document);
const btnSubmitInfor = $("#submit1");
const btnSubmitChangePass = $("#submit-change-pass");

async function FormInforHanlde() {
  const form = document.getElementById("form-infor");
  const formData = new FormData(form);
  const data = {};
  console.log("here");
  let urlImg;
  if (formData.get("avatar").name) {
    urlImg = await uploadImage(formData.get("avatar"));
  }

  formData.forEach((value, key) => {
    // Thực hiện các xử lý khác với giá trị tại đây
    if (key !== "avatar") {
      data[key] = value;
    } else if (value.name) {
      // check if file is uploaded
      data[key] = urlImg;
    }
    console.log(data);
  });
  data.gender = data.gender === "1" ? 1 : 0;
  axios.patch("/api/v1/users", data).then((res) => {
    console.log(res);
    if (res.status === 200) {
      window.location.href = "/profile/edit";
    }
  });
  console.log(data);
}

btnSubmitInfor.onclick = FormInforHanlde;
// btnSubmitChangePass.onclick = changePassword;
btnSubmitChangePass.addEventListener("click", (e) => {
  e.preventDefault();
  try {
    changePassword();
  } catch (error) {
    // nếu thất bại thì hiện thông báo lỗi
  }
});
// change password
async function changePassword() {
  const form = document.querySelector("#form-change-password");
  const formData = new FormData(form);
  console.log(formData.get("confirm-password"), formData.get("newPassword"));

  if (formData.get("confirm-password") !== formData.get("newPassword"))
    throw new Error("Mật khẩu xác nhận không đúng!");
  const data = {
    oldPassword: formData.get("oldPassword"),
    newPassword: formData.get("newPassword"),
  };
  try {
    // gửi request đỗi mật khẩu
    const { status, response } = await axios.post(
      `/api/v1/auth/change-password`,
      data
    );
    // nếu thành công thì hiện thông báo
    if (status === 200) {
      let text = document.createElement("p");
      text.type = "button";
      text.className = "text-primary";
      text.textContent = "thay đỗi mật khẩu thành công...";
      form.appendChild(text);
      setTimeout(() => {
        text.remove();
        form.reset();
      }, 3000);
    }
  } catch (error) {
    let text = document.createElement("p");
    text.type = "button";
    text.className = "text-danger";
    text.textContent = "thay đỗi mật khẩu thất bại...";
    form.appendChild(text);
    setTimeout(() => {
      text.remove();
      form.reset();
    }, 3000);
  }
}
