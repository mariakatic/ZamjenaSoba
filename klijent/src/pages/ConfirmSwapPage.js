import React, { useState, useEffect, useContext } from "react";
import { Layout } from "antd";
import { Button, Card, Form, notification } from "antd";
import { Link, useParams } from "react-router-dom";
import PostConfirm from "../components/Post/PostConfirm";
import Title from "antd/lib/skeleton/Title";
import { Redirect } from "react-router-dom";
import axios from "axios";
import NavBar from "../components/NavBar/NavBar";

// import { TokenContext } from "../Context";
import { InfoContext } from "../Context";

const { Content, Footer } = Layout;

export default function ConfirmSwapPage() {
  //let { token, id_oglas_moj, id_oglas_tudi } = useParams();

  // const { token, setToken } = useContext(TokenContext);
  const { info, setInfo } = useContext(InfoContext);

  const [posts, setPosts] = useState([]);
  //const [idLanac, setIdLanac] = useState([]);

  const Confirm = () => {
    axios
      .post(`${process.env.REACT_APP_API_URL}/api/v1/potvrdiZamjenu`, {
        token: info.token,
        //idLanac: idLanac,
      })
      .then((res) => {
        notification.open({
          message: "Uspješno zatražena zamjena!",
          description: "Biti ćete obavješteni putem maila o izvršenoj zamjeni.",
        });
      });
  };

  const Cancel = () => {
    axios
      .post(`${process.env.REACT_APP_API_URL}/api/v1/odbijZamjenu`, {
        token: info.token,
      })
      .then((res) => {
        notification.open({
          message: "Uspješno odbačena zamjena!",
        });
      });
  };

  const getPosts = () => {
    var posts = [];

    //dobivam idLanac, rednibroj, oglas za mog studenta
    axios
      .get(`${process.env.REACT_APP_API_URL}/api/v1/zamjene`, {
        //provjeriti sve sa backendom
        headers: {
          Authorization: "Bearer " + info.token, //the token is a variable which holds the token
        },
      })
      .then((res) => {
        posts = res.data;
        setPosts(posts);
        //setIdLanac(res.data.idLanca);
      });
  };

  useEffect(() => {
    getPosts();
  }, []);

  return (
    <React.Fragment>
      <Layout className="layout" style={{ minHeight: "100vh" }}>
        <NavBar />
        {/* <div className="nav" style={{justifyContent: 'left'}}>
                <img alt="logo" src="/room.png" className="navLogo" />
                <Link to="/">
                    <span className="navName" >RoomSwitchr</span>
                </Link>
            </div> */}

        <Content style={{ padding: "0 50px" }}>
          <br />

          {posts.length != 0 ? (
            posts.map((post) => (
              <div
                className="site-layout-content"
                style={{ maxWidth: "720px", margin: "0 auto" }}
              >
                <PostConfirm
                  key={post.oglas.idOglas}
                  idOglas={post.oglas.idOglas}
                  kat={post.oglas.kat}
                  paviljon={post.oglas.paviljon}
                  kategorija_sobe={post.oglas.kategorijaSobe}
                  title={post.oglas.dom.imeDoma}
                  idLanac={post.idLanca}
                  username={post.oglas.student.username}
                  email={post.oglas.student.email}
                />
                <br />
              </div>
            ))
          ) : (
            <Card
              style={{ maxWidth: "720px", margin: "auto", textAlign: "center" }}
            >
              <p>Nema oglasa za prikaz.</p>
            </Card>
          )}
        </Content>

        <Footer style={{ textAlign: "center" }}>
          PiccoloGrupa © 2020 FER PI
        </Footer>
      </Layout>
    </React.Fragment>
  );
}
