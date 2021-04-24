import React, { useContext } from "react";
import { Layout } from "antd";
import NavBar from "../components/NavBar/NavBar";
import NewPostComponent from "../components/NewPostComponent";

// import { TokenContext } from "../Context";
import { InfoContext } from "../Context";

const { Content, Footer } = Layout;

export default function NewPostPage() {
  // const { token, setToken } = useContext(TokenContext);
  const { info, setInfo } = useContext(InfoContext);

  return (
    <Layout className="layout" style={{ minHeight: "100vh" }}>
      <NavBar />
      <Content
        className="site-layout-content"
        style={{ maxWidth: "720px", margin: "auto", padding: "2em" }}
      >
        <NewPostComponent token={info.token} />
      </Content>

      <Footer style={{ textAlign: "center" }}>
        PiccoloGrupa © 2020 FER PI
      </Footer>
    </Layout>
  );
}
