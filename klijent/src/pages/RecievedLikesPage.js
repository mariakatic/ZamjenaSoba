import React, { useContext, useState, useEffect } from "react";
import { Layout, Typography, Input, Select } from "antd";
import NavBar from "../components/NavBar/NavBar";
import { Button, Card } from "antd";
import axios from "axios";
import Post from "../components/Post/Post";

// import { TokenContext } from "../Context";
import { InfoContext } from "../Context";

const { Content, Footer } = Layout;
const { Title } = Typography;

export default function MyPostsPage() {
  // const { token, setToken } = useContext(TokenContext);
  const { info, setInfo } = useContext(InfoContext);
  const [posts, setPosts] = useState([]);

  const getPosts = () => {
    var posts = [];
    axios
      .get(
        `${process.env.REACT_APP_API_URL}/api/home/oglasiStudenataKojiSuLajkali`,
        {
          //provjeriti sve sa backendom
          headers: {
            Authorization: "Bearer " + info.token, //the token is a variable which holds the token
          },
        }
      )
      .then((res) => {
        posts = res.data;
        setPosts(posts);
      });
  };

  useEffect(() => {
    getPosts();
  }, []);

  return (
    <Layout className="layout" style={{ minHeight: "100vh" }}>
      <NavBar />
      <Content>
        <br />
        <Card style={{ maxWidth: "720px", margin: "auto", padding: "2em" }}>
          <Title level={3}>
            Oglasi osoba koje su označile vaš oglas sa "sviđa mi se"
          </Title>
        </Card>
        <br />
        {posts.length != 0 ? (
          posts.map((post) => (
            <Post
              key={post.oglas.idOglas}
              kat={post.oglas.kat}
              paviljon={post.oglas.paviljon}
              kategorija_sobe={post.oglas.kategorijaSobe}
              title={post.oglas.dom.imeDoma}
              idOglas={post.oglas.idOglas}
              lajk={post.stupanjLajkanja}
              username={post.oglas.student.username}
              email={post.oglas.student.email}
            />
          ))
        ) : (
          <Card
            style={{ maxWidth: "720px", margin: "auto", textAlign: "center" }}
          >
            <p>Nema oglasa za prikaz.</p>
          </Card>
        )}

        <br />
      </Content>

      <Footer style={{ textAlign: "center" }}>
        PiccoloGrupa © 2020 FER PI
      </Footer>
    </Layout>
  );
}
