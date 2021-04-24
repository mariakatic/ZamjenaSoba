import React, { useContext, useState, useEffect } from "react";
import { Layout, Typography, Input, Select } from "antd";
import NavBar from "../components/NavBar/NavBar";
import { Button, Card } from "antd";
import { EditOutlined } from "@ant-design/icons";
import { Link } from "react-router-dom";
import axios from "axios";
import MyPost from "../components/Post/MyPost";

// import { TokenContext } from "../Context";
import { InfoContext } from "../Context";

const { Content, Footer } = Layout;
const { Title } = Typography;
const { TextArea } = Input;
const { Option } = Select;

export default function MyPostsPage() {
  // const { token, setToken } = useContext(TokenContext);
  const { info, setInfo } = useContext(InfoContext);

  const [posts, setPosts] = useState([]);
  const [currentEditPost, setCurrentEditPost] = useState(false);

  const getPosts = () => {
    var posts = [];
    axios
      .get(`${process.env.REACT_APP_API_URL}/api/home/vlastitiOglasi`, {
        headers: {
          Authorization: "Bearer " + info.token, //the token is a variable which holds the token
        },
      })
      .then((res) => {
        posts = res.data;
        setPosts(res.data);
      });
  };

  useEffect(() => {
    getPosts();
  }, []);

  const meta = {
    columns: 1,
    fields: [{ key: "izvuc grad", label: "Grad", requried: true }], //nije dobro tako, nego treba sve izvuc odjednom iz baze u jedan json
  };

  return (
    <Layout className="layout" style={{ minHeight: "100vh" }}>
      <NavBar />
      <Content>
        {/* <div
          className="site-layout-content"
          style={{ maxWidth: "720px", margin: "0 auto" }}
        > */}
        <br />
        <Card style={{ maxWidth: "720px", margin: "auto", padding: "2em" }}>
          <Title level={3}>Moji oglasi</Title>
          <Link to="/newpost">
            <Button type="primary">
              Stvori novi oglas
              <EditOutlined />
            </Button>
          </Link>
        </Card>
        <br />
        {posts.map((post) => (
          <MyPost
            key={post.idOglas}
            kat={post.kat}
            idOglas={post.idOglas}
            paviljon={post.paviljon}
            kategorija_sobe={post.kategorijaSobe}
            title={post.dom.imeDoma}
            idDom={post.dom.idDom}
            aktivan={post.aktivan}
          />
        ))}
        <br />
      </Content>

      <Footer style={{ textAlign: "center" }}>
        PiccoloGrupa © 2020 FER PI
      </Footer>
    </Layout>
  );
}
