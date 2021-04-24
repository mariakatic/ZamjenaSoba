import React, { useContext } from "react";
import { Layout, Typography, Input, Select } from "antd";
import NavBar from "../components/NavBar/NavBar";
import { Button, Card } from "antd";
import { EditOutlined } from "@ant-design/icons";
import { Link } from "react-router-dom";
import FormBuilder from "antd-form-builder";

// import { TokenContext } from "../Context";

const { Content, Footer } = Layout;
const { Title } = Typography;
const { TextArea } = Input;
const { Option } = Select;

export default function MyPostsPage() {
  //const { token, setToken } = useContext(TokenContext);

  const USER_ACTIVE_POST = {
    // axios
    //       .get("https://room-switchr.herokuapp.com/api/test/currentuser", {
    //         // headers: {
    //         //   Authorization: "Bearer " + token, //the token is a variable which holds the token
    //         // },
    //       })
    //       .then((res) => {
    //         const korisnicko_ime = JSON.stringify(res.data);        //trenutno ne radi
    //         setUsername(korisnicko_ime)
    //       });
  };
  const USER_NOT_ACTIVE_POSTS = {};

  const meta = {
    columns: 1,
    fields: [{ key: "izvuc grad", label: "Grad", requried: true }], //nije dobro tako, nego treba sve izvuc odjednom iz baze u jedan json
  };

  return (
    <Layout className="layout" style={{ minHeight: "100vh" }}>
      <NavBar />
      <Content style={{ maxWidth: "720px", margin: "auto", padding: "2em" }}>
        {/* <div
          className="site-layout-content"
          style={{ maxWidth: "720px", margin: "0 auto" }}
        > */}
        <Card>
          <Title>
            Moji oglasi &nbsp;&nbsp;&nbsp;&nbsp;
            <Link to="/newpost">
              <Button type="primary">
                Stvori novi oglas
                <EditOutlined />
              </Button>
            </Link>
          </Title>

          {/* ovdje ispisati njegove oglase, prvo onaj aktivni */}
          <Card>
            <h3>AKTIVAN OGLAS</h3>
            <FormBuilder
              name="aktivan_oglas"
              meta={meta}
              values={USER_ACTIVE_POST}
              viewMode={true}
            />
            <Link to="/editpost" value={meta.fields.key}>
              <Button type="primary" style={{ padding: "5px" }}>
                Uredi
                <EditOutlined />
              </Button>
            </Link>
          </Card>
          <br></br>

          {/* napraviti for petlju za svaki ostali oglas */}
          <Card>
            <FormBuilder
              name="neaktivan_oglas"
              meta={meta}
              values={USER_NOT_ACTIVE_POSTS}
              viewMode={true}
            />
            <Link to="/editpost" value={meta.fields.key}>
              {/* za uređivanje poslati value="id post" */}
              <Button type="primary" style={{ padding: "5px" }}>
                Uredi
                <EditOutlined />
              </Button>
            </Link>
          </Card>
          <br></br>
        </Card>
      </Content>

      <Footer style={{ textAlign: "center" }}>
        PiccoloGrupa © 2020 FER PI
      </Footer>
    </Layout>
  );
}
