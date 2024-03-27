// const emailInput = document.getElementById("exampleInputEmail1");
// const passInput = document.getElementById("exampleInputPassword1");
// const formCheckBtn = document.querySelector(".btn");

// const loginHandler = () => {
//   axios
//     .post("/api/v1/auth/login", {
//       // email: "testUser123@gmail.com",
//       // password: "123456",
//       email: emailInput.value,
//       password: passInput.value,
//     })
//     .then(function (response) {
//       console.log(response.data);
//       Cookies.set("access_token", response.data.access_token, {
//         expires: 7,
//         path: "/",
//       });
//       console.log(Cookies.get("access_token"));
//     })
//     .catch(function (error) {
//       console.log(error);
//     });
// };
// console.log(`formCheckBtn ${formCheckBtn} `);

// formCheckBtn.addEventListener("click", (e) => {
//   e.preventDefault();
//   console.log("after click");
//   loginHandler();
//   formCheckBtn.submit();
// });
