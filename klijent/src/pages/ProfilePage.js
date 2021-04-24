import React, { useState, useContext, useEffect } from "react";
import { Layout, Typography, Card } from "antd";
import { EditOutlined } from "@ant-design/icons";
import NavBar from "../components/NavBar/NavBar";
import EditUser from "../components/EditUser";
import axios from "axios";

// import { TokenContext } from "../Context";
import { InfoContext } from "../Context";

const { Content, Footer } = Layout;
const { Title } = Typography;

export default function ProfilePage() {
  // const { token, setToken } = useContext(TokenContext);
  const { info, setInfo } = useContext(InfoContext);

  const [editUserDrawerVisible, setEditUserDrawerVisible] = useState(false);
  const [username, setUsername] = useState(null);
  const [name, setName] = useState(null);
  const [jmbag, setJmbag] = useState(null);
  const [email, setEmail] = useState(null);
  const [id, setId] = useState(null);

  const showDrawer = () => {
    setEditUserDrawerVisible(true);
  };

  const onCloseDrawer = () => {
    setEditUserDrawerVisible(false);
  };

  const getData = () => {
    axios
      .get(`${process.env.REACT_APP_API_URL}/api/v1/currentuser`, {
        headers: {
          Authorization: "Bearer " + info.token, //the token is a variable which holds the token
        },
      })
      .then((res) => {
        setUsername(res.data.username);
        setName(res.data.name);
        setJmbag(res.data.jmbag);
        setEmail(res.data.email);
        setId(res.data.id);
      });
  };

  useEffect(() => {
    getData();
  }, []);

  return (
    <Layout className="layout" style={{ minHeight: "100vh" }}>
      <NavBar />
      <Content>
        <br />
        <Card
          style={{ maxWidth: "720px", margin: "auto" }}
          actions={[
            <div onClick={() => showDrawer()}>
              <EditOutlined /> uredi
            </div>,
          ]}
        >
          <Title>Moj profil</Title>
          <br />
          <p>Korisničko ime: {username}</p>
          <p>Email: {email}</p>
          <p>Ime i prezime: {name}</p>
          <p>JMBAG: {jmbag}</p>
        </Card>
        <EditUser
          visible={editUserDrawerVisible}
          closeHandler={() => onCloseDrawer()}
          username={username}
          name={name}
          email={email}
          jmbag={jmbag}
          id={id}
          token={info.token}
        />
      </Content>
      <Footer style={{ textAlign: "center" }}>
        PiccoloGrupa © 2020 FER PI
      </Footer>
    </Layout>
  );
}
