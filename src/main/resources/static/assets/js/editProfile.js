import { uploadImage } from "./uploadFileService.js";
const $ = document.querySelector.bind(document);
const $$ = document.querySelectorAll.bind(document);

const firstName = $("#fname");
const lastName = $("#lname");
const dateOfBirth = $("#dob");
const btnSubmitInfor = $("#submit1");

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
