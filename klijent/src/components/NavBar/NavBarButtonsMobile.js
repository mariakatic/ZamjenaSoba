import React, { useContext, useState, useEffect } from "react";
import { Drawer, Button, Badge } from "antd";
import {
  HomeOutlined,
  UserOutlined,
  UserAddOutlined,
  MenuOutlined,
} from "@ant-design/icons";
import "./NavBar.css";
import { Link } from "react-router-dom";

// import { LoginContext } from "../../Context";
// import { TokenContext } from "../../Context";
// import { RoleContext } from "../../Context";
import { InfoContext } from "../../Context";

function NavBarButtons(props) {
  // const { loggedIn, setLoggedIn } = useContext(LoginContext);
  // const { token, setToken } = useContext(TokenContext);
  // const { role, setRole } = useContext(RoleContext);
  const { info, setInfo } = useContext(InfoContext);

  const [visible, setVisible] = useState(false);
  // const[childrenDrawer, setchildrenDrawer] = useState(false);

  const showDrawer = () => {
    setVisible(true);
  };

  const onClose = () => {
    setVisible(false);
  };

  const renderSwitch = (loggedIn, role) => {
    console.log("loggedIn, role");
    console.log(loggedIn);
    console.log(role);
    if (loggedIn && role == "STUDENT") {
      console.log("JA SAM STUDENT");

      return (
        <>
          <React.Fragment>
            <Link to="/">
              <Button type="text" className="navButtonMobile" onClick={onClose}>
                <HomeOutlined />
                Početna
              </Button>
            </Link>

            <Link to="/myposts">
              <Button type="text" className="navButtonMobile" onClick={onClose}>
                <UserOutlined />
                Moji oglasi
              </Button>
            </Link>

            <Link to="/profile">
              <Button type="text" className="navButtonMobile" onClick={onClose}>
                <UserOutlined />
                Uredi profil
              </Button>
            </Link>

            <Link to="/recievedlikesposts">
              <Button
                type="text"
                className="navButtonMobile"
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
                className="navButtonMobile"
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
                className="navButtonMobile"
                onClick={() => {
                  // setLoggedIn(false);
                  setVisible(true);
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
      console.log("JA SAM DJELATNIK");
      return (
        <React.Fragment>
          {" "}
          <Link to="/">
            <Button
              type="text"
              className="navButtonMobile"
              onClick={() => {
                // setLoggedIn(false);
                setVisible(true);
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
      );
    } else {
      console.log("JA NISAM NIŠTA");

      return (
        <React.Fragment>
          <Link to="/">
            <Button type="text" className="navButtonMobile" onClick={onClose}>
              <HomeOutlined />
              Početna
            </Button>
          </Link>

          <Link to="/login">
            <Button type="text" className="navButtonMobile" onClick={onClose}>
              <UserOutlined />
              Prijavi se
            </Button>
          </Link>

          <Link to="/register">
            <Button type="text" className="navButtonMobile" onClick={onClose}>
              <UserAddOutlined />
              Registriraj se
            </Button>
          </Link>
        </React.Fragment>
      );
    }
  };

  return (
    <div>
      <Button
        className="navContainerMobile"
        type="text"
        onClick={showDrawer}
        style={{ color: "#fff" }}
      >
        <MenuOutlined style={{ fontSize: "24px" }} />
      </Button>
      <Drawer
        title="RoomSwitchr"
        width={200}
        closable={true}
        onClose={onClose}
        visible={visible}
      >
        {renderSwitch(info.loggedIn, info.role)}
      </Drawer>
    </div>
  );
}

export default NavBarButtons;
