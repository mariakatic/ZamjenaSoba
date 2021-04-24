import React, { useState, useContext, useEffect } from "react";
import "./NavBar.css";
import { Link } from "react-router-dom";
import NavBarButtons from "./NavBarButtons";
import NavBarButtonsMobile from "./NavBarButtonsMobile";
import axios from "axios";
// import { LoginContext } from "../../Context";
// import { TokenContext } from "../../Context";
// import { RoleContext } from "../../Context";
import { InfoContext } from "../../Context";

function NavBar() {
  // const { loggedIn, setLoggedIn } = useContext(LoginContext);
  // const { token, setToken } = useContext(TokenContext);
  // const { role, setRole } = useContext(RoleContext);
  const { info, setInfo } = useContext(InfoContext);

  const [likes, setLikes] = useState(0);
  const [swaps, setSwaps] = useState(0);

  const getLikes = () => {
    axios
      .get(`${process.env.REACT_APP_API_URL}/api/home/notification/like`, {
        headers: {
          Authorization: "Bearer " + info.token, //the token is a variable which holds the token
        },
      })
      .then((res) => {
        setLikes(res.data);
      })
      .catch((res) => {});
  };

  const getSwaps = () => {
    axios
      .get(`${process.env.REACT_APP_API_URL}/api/home/notification/swap`, {
        headers: {
          Authorization: "Bearer " + info.token, //the token is a variable which holds the token
        },
      })
      .then((res) => {
        setSwaps(res.data);
      })
      .catch((res) => {});
  };

  const removeLikes = () => {
    axios
      .post(
        `${process.env.REACT_APP_API_URL}/api/home/notification/like`,
        {},
        {
          headers: {
            Authorization: "Bearer " + info.token, //the token is a variable which holds the token
          },
        }
      )
      .then((res) => {
        setLikes(0);
      })
      .catch((res) => {});
  };

  const removeSwaps = () => {
    axios
      .post(
        `${process.env.REACT_APP_API_URL}/api/home/notification/swap`,
        {},
        {
          headers: {
            Authorization: "Bearer " + info.token, //the token is a variable which holds the token
          },
        }
      )
      .then((res) => {
        setSwaps(0);
      })
      .catch((res) => {});
  };

  useEffect(() => {
    console.log(info.role);
    if (info.loggedIn && info.role == "STUDENT") {
      getLikes();
      getSwaps();
    }
  }, []);

  return (
    <div className="nav">
      <img alt="logo" src="/room.png" className="navLogo" />
      <Link to="/">
        <span className="navName">RoomSwitchr</span>
      </Link>
      <NavBarButtons
        likes={likes}
        swaps={swaps}
        removeLikes={removeLikes}
        removeSwaps={removeSwaps}
      />
      <NavBarButtonsMobile
        likes={likes}
        swaps={swaps}
        removeLikes={removeLikes}
        removeSwaps={removeSwaps}
      />
    </div>
  );
}

export default NavBar;
