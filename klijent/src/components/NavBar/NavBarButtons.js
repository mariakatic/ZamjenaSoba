import React, { useContext, useState, useEffect } from "react";
import { Button, Badge } from "antd";
import { HomeOutlined, UserOutlined, UserAddOutlined } from "@ant-design/icons";
import "./NavBar.css";
import { Link } from "react-router-dom";

// import { LoginContext } from "../../Context";
// import { TokenContext } from "../../Context";
// import { RoleContext } from "../../Context";
import { InfoContext } from "../../Context";

function NavBarButtons(props) {
  const { info, setInfo } = useContext(InfoContext);
  // const { loggedIn, setLoggedIn } = useContext(LoginContext);
  // const { token, setToken } = useContext(TokenContext);
  // const { role, setRole } = useContext(RoleContext);

  useEffect(() => {
    // console.log("role2");

    // console.log(role);
  }, []);

  const renderSwitch = (loggedIn, role) => {
    // console.log("loggedIn, role");
    // console.log(loggedIn);
    // console.log(role);
    if (loggedIn && role == "STUDENT") {
      return (
        <>
          <React.Fragment>
            <Link to="/">
              <Button type="text" className="navButton">
                <HomeOutlined />
                Početna
              </Button>
            </Link>

            <Link to="/myposts">
              <Button type="text" className="navButton">
                <UserOutlined />
                Moji oglasi
              </Button>
            </Link>

            <Link to="/profile">
              <Button type="text" className="navButton">
                <UserOutlined />
                Uredi profil
              </Button>
            </Link>

            <Link to="/recievedlikesposts">
              <Button
                type="text"
                className="navButton"
                onClick={() => props.removeLikes()}
              >
                <UserOutlined />
                Dobiveni lajkovi
                <Badge count={props.likes} dot />
              </Button>
            </Link>

            <Link to="/confirmswap">
              <Button
                type="text"
                className="navButton"
                onClick={() => props.removeSwaps()}
              >
                <UserOutlined />
                Moguće zamjene
                <Badge count={props.swaps} dot />
              </Button>
            </Link>

            <Link to="/">
              <Button
                type="text"
                className="navButton"
                onClick={() => {
                  // setLoggedIn(false);
                  // setRole(false);
                  // setToken("");
                  setInfo({loggedIn:false, role:false, token:""});
                }}
              >
                <UserOutlined />
                Odjava
              </Button>
            </Link>
          </React.Fragment>
        </>
      );
    } else if (loggedIn) {
      return (
        <>
          <React.Fragment>
            <Link to="/">
              <Button
                type="text"
                className="navButton"
                onClick={() => {
                  // setLoggedIn(false);
                  // setRole(false);
                  // setToken("");
                  setInfo({loggedIn:false, role:false, token:""});
                }}
              >
                <UserOutlined />
                Odjava
              </Button>
            </Link>
          </React.Fragment>
        </>
      );
    } else {
      return (
        <React.Fragment>
          <Link to="/">
            <Button type="text" className="navButton">
              <HomeOutlined />
              Početna
            </Button>
          </Link>

          <Link to="/login">
            <Button type="text" className="navButton">
              <UserOutlined />
              Prijavi se
            </Button>
          </Link>

          <Link to="/register">
            <Button type="text" className="navButton">
              <UserAddOutlined />
              Registriraj se
            </Button>
          </Link>
        </React.Fragment>
      );
    }
  };

  return <div className="navContainer">{renderSwitch(info.loggedIn, info.role)}</div>;
}

export default NavBarButtons;
