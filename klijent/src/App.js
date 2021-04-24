import React, { useState } from "react";
import { Route, Switch } from "react-router-dom";

import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import NewPostPage from "./pages/NewPostPage";
import ProfilePage from "./pages/ProfilePage";
import MyPostsPage from "./pages/MyPostsPage";
import EditPostPage from "./pages/EditPostPage";
import ConfirmSwapPage from "./pages/ConfirmSwapPage";
import RecievedLikesPage from "./pages/RecievedLikesPage";
import SCDjelatnikPage from "./pages/SCDjelatnikPage";
import dotenv from "dotenv";

// import objaveService from './services/ObjaveService';

// import { LoginContext } from "./Context";
// import { TokenContext } from "./Context";
// import { RoleContext } from "./Context";
import { InfoProvider } from "./Context";


export default function App() {
  require("dotenv").config();

  // state = {
  //   objave: []
  // };

  // componentDidMount(){
  //     objaveService.getObjave().then((res) => {
  //     this.setState({ objave: res.data})
  //   });
  // }

  //render() {
  // const [loggedIn, setLoggedIn] = useState(false);
  // const [token, setToken] = useState("");
  // const [role, setRole] = useState("");

  const App = () => (
    <div>
      <InfoProvider>
      {/* <LoginContext.Provider value={{ loggedIn, setLoggedIn }}>
        <TokenContext.Provider value={{ token, setToken }}>
          <RoleContext.Provider value={{ role, setRole }}> */}
            <Route exact path="/" component={HomePage} />
            <Route exact path="/login" component={LoginPage} />
            <Route exact path="/register" component={RegisterPage} />
            <Route exact path="/myposts" component={MyPostsPage} />
            <Route exact path="/newpost" component={NewPostPage} />
            <Route exact path="/editpost" component={EditPostPage} />
            <Route exact path="/profile" component={ProfilePage} />
            <Route exact path="/confirmswap" component={ConfirmSwapPage} />
            <Route
              exact
              path="/recievedlikesposts"
              component={RecievedLikesPage}
            />
            <Route exact path="/scdjelatnik" component={SCDjelatnikPage} />
          {/* </RoleContext.Provider>
        </TokenContext.Provider>
      </LoginContext.Provider> */}
      </InfoProvider>
    </div>
  );
  return (
    <Switch basename={process.env.PUBLIC_URL}>
      <App />
    </Switch>
  );
  //}
}

//export default App;
