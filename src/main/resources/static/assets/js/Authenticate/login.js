const emailInput = document.getElementById("exampleInputEmail1");
const passInput = document.getElementById("exampleInputPassword1");
const formCheckBtn = document.querySelector(".btn");

const loginHandler = () => {
  axios
    .post("/api/v1/auth/login", {
      // email: "testUser123@gmail.com",
      // password: "123456",
      email: emailInput.value,
      password: passInput.value,
    })
    .then(function (response) {
      console.log(response.data);
      const access_token = response.data.access_token;
      Cookies.set("access_token", response.data.access_token, {
        expires: 7,
        path: "/",
      });
      console.log(Cookies.get("access_token"));
      // redirect into home
    })
    .then(
      //   axios
      //     .get("/home/index", {
      //       headers: {
      //         Authorization: "Bearer " + Cookies.get("access_token"),
      //       },
      //     })
      //     .then(function (response) {
      //       // handle success
      //       console.log("success");
      //     })
      () => {
        window.location.href = "/home/index";
      }
    )

    .catch(function (error) {
      console.log(error);
    });
};
console.log(`formCheckBtn ${formCheckBtn} `);

formCheckBtn.addEventListener("click", (e) => {
  console.log("after click");
  e.preventDefault();
  loginHandler();
  axios.get("");
});
