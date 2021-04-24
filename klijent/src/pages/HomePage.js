import React, { useContext, useEffect, useState } from "react";
import { Layout, Form, Input, Button } from "antd";
import NavBar from "../components/NavBar/NavBar";
import Post from "../components/Post/Post";
import MyPost from "../components/Post/MyPost";
import axios from "axios";
import { Collapse, Select } from "antd";

// import { LoginContext } from "../Context";
// import { TokenContext } from "../Context";
import { InfoContext } from "../Context";

const { Content, Footer } = Layout;
const { Panel } = Collapse;
const { Option } = Select;

export default function HomePage() {
  // const { loggedIn, setLoggedIn } = useContext(LoginContext);
  // const { token, setToken } = useContext(TokenContext);
  const { info, setInfo } = useContext(InfoContext);
  const [allPosts, setAllPosts] = useState([]);
  const [visiblePosts, setVisiblePosts] = useState([]);
  const [id, setID] = useState([]);
  const [submitButton, setSubmitButton] = useState(0);
  const [domovi, setDomovi] = useState([]);

  const layout = {
    labelCol: { span: 4 },
    wrapperCol: { span: 20 },
  };

  const [applyFilterDrawerVisible, setApplyFilterDrawerVisible] = useState(
    false
  );

  const showDrawer = () => {
    setApplyFilterDrawerVisible(true);
  };

  const onCloseDrawer = () => {
    setApplyFilterDrawerVisible(false);
  };

  const getPosts = () => {
    if (info.loggedIn) {
      axios
        .get(`${process.env.REACT_APP_API_URL}/api/v1/currentuser/`, {
          headers: {
            Authorization: "Bearer " + info.token, //the token is a variable which holds the token
          },
        })
        .then((res) => {
          setID(res.data.id);
        });
    }

    axios
      .get(`${process.env.REACT_APP_API_URL}/api/home/`, {
        headers: {
          Authorization: "Bearer " + info.token, //the token is a variable which holds the token
        },
      })
      .then((res) => {
        setAllPosts(res.data);
        setVisiblePosts(res.data);
      });
  };

  useEffect(() => {
    getPosts();
    var domovi = [];
    axios.get(`${process.env.REACT_APP_API_URL}/api/v1/dom`).then((res) => {
      domovi = res.data;
      setDomovi(domovi);
    });
  }, []);

  let postsObjects = visiblePosts.map((post) => {
    if (info.loggedIn) {
      if (post.oglas.student.id === id) {
        return (
          <MyPost
            key={post.oglas.idOglas}
            kat={post.oglas.kat}
            idOglas={post.oglas.idOglas}
            paviljon={post.oglas.paviljon}
            kategorija_sobe={post.oglas.kategorijaSobe}
            title={post.oglas.dom.imeDoma}
            idDom={post.oglas.dom.idDom}
            aktivan={post.oglas.aktivan}
          />
        );
      } else {
        return (
          <Post
            key={post.oglas.idOglas}
            kat={post.oglas.kat}
            idOglas={post.oglas.idOglas}
            paviljon={post.oglas.paviljon}
            kategorija_sobe={post.oglas.kategorijaSobe}
            title={post.oglas.dom.imeDoma}
            lajk={post.stupanjLajkanja}
            username={post.oglas.student.username}
            email={post.oglas.student.email}
          />
        );
      }
    } else {
      return (
        <Post
          key={post.oglas.idOglas}
          kat={post.oglas.kat}
          idOglas={post.oglas.idOglas}
          paviljon={post.oglas.paviljon}
          kategorija_sobe={post.oglas.kategorijaSobe}
          title={post.oglas.dom.imeDoma}
          username={post.oglas.student.username}
          email={post.oglas.student.email}
        />
      );
    }
  });
  const [form] = Form.useForm();
  const handleSearchChange = (values) => {
    if (submitButton == 1) {
      axios
        .get(`${process.env.REACT_APP_API_URL}/api/home/filter`, {
          headers: {
            Authorization: "Bearer " + info.token, //the token is a variable which holds the token
          },
        })
        .then((res) => {
          console.log(res.data);
          let p,kat,kateg;
          if (res.data.paviljon !== 0){
            p = res.data.paviljon
          }
          if (res.data.kat !== 0){
            kat = res.data.kat
          }
          if (res.data.kategorijaSobe !== 0){
            kateg = res.data.kategorijaSobe
          }
          form.setFieldsValue({
            domQuery: res.data.dom,
            paviljonQuery: p,
            katQuery: kat,
            kategorijaSobeQuery: kateg,
          });
        })
        .catch((res) => {});
    } else if (submitButton == 2) {
      console.log(values);
      let values2 = {
        dom: values.domQuery,
        paviljon: values.paviljonQuery,
        kat: values.katQuery,
        kategorijaSobe: values.kategorijaSobeQuery,
      };
      axios
        .post(`${process.env.REACT_APP_API_URL}/api/home/filter`, values2, {
          headers: {
            Authorization: "Bearer " + info.token, //the token is a variable which holds the token
          },
        })
        .then((res) => {})
        .catch((res) => {});
    } else {
      let visiblePostsTmp = [];
      allPosts.forEach((post) => {
        let dom_kat_kategorija_paviljon = [
          values.domQuery,
          values.katQuery,
          values.kategorijaSobeQuery,
          values.paviljonQuery,
        ];
        if (
          typeof values.domQuery !== "undefined" ||
          typeof values.katQuery !== "undefined" ||
          typeof values.kategorijaSobeQuery !== "undefined" ||
          typeof values.paviljonQuery !== "undefined"
        ) {
          let count = 0;
          let odbacujem_post = false;
          dom_kat_kategorija_paviljon.forEach((el) => {
            if (el) {
              if (count == 0) {
                if (!post.oglas.dom.imeDoma.includes(values.domQuery)) {
                  odbacujem_post = true;
                }
              } else if (count == 1) {
                if (!post.oglas.kat.toString().includes(values.katQuery)) {
                  odbacujem_post = true;
                }
              } else if (count == 2) {
                if (
                  !post.oglas.kategorijaSobe
                    .toString()
                    .includes(values.kategorijaSobeQuery)
                ) {
                  odbacujem_post = true;
                }
              } else if (count == 3) {
                if (
                  !post.oglas.paviljon.toString().includes(values.paviljonQuery)
                ) {
                  odbacujem_post = true;
                }
              }
            }
            count++;
          });
          if (!odbacujem_post) {
            visiblePostsTmp.push(post);
          }
        }
      });

      if (
        (values.paviljonQuery === undefined || values.paviljonQuery === "") &&
        (values.katQuery === undefined || values.katQuery === "") &&
        (values.kategorijaSobeQuery === undefined ||
          values.kategorijaSobeQuery === "") &&
        (values.domQuery === undefined || values.domQuery === "")
      ) {
        visiblePostsTmp = allPosts;
      }

      setVisiblePosts(visiblePostsTmp);
    }
  };

  let optionDomovi = domovi.map((dom) => (
    <Option value={dom.imeDoma}>{dom.imeDoma}</Option>
  ));

  return (
    <Layout className="layout" style={{ minHeight: "100vh" }}>
      <NavBar />
      <Content style={{ padding: "0 50px" }}>
        <div
          className="site-layout-content"
          style={{ maxWidth: "720px", margin: "0 auto" }}
        >
          <br />

          <Collapse defaultActiveKey={["0"]} onChange={() => {}}>
            <Panel header="Filtriranje oglasa" key="1">
              <Form
                {...layout}
                name="register"
                onFinish={handleSearchChange}
                form={form}
                initialValues={{
                  residence: ["zhejiang", "hangzhou", "xihu"],
                  prefix: "86",
                }}
                scrollToFirstError
              >
                <Form.Item name="domQuery" label="Dom">
                  <Select placeholder="Dom">
                    {optionDomovi}
                    <Option value={null}>Ne filtriraj prema domu</Option>
                  </Select>
                </Form.Item>

                <Form.Item name="paviljonQuery" label="Paviljon">
                  <Input />
                </Form.Item>

                <Form.Item name="katQuery" label="Kat">
                  <Input />
                </Form.Item>

                <Form.Item name="kategorijaSobeQuery" label="Kategorija Sobe">
                  <Input />
                </Form.Item>

                <Form.Item style={{ textAlign: "right" }}>
                  <Button
                    type="primary"
                    htmlType="submit"
                    className="login-form-button"
                    onClick={() => setSubmitButton(3)}
                  >
                    Pretraži
                  </Button>
                  {info.loggedIn ? (
                    <>
                      <Button
                        type="primary"
                        htmlType="submit"
                        className="login-form-button"
                        onClick={() => setSubmitButton(2)}
                        style={{ margin: "10px" }}
                      >
                        Spremi filter
                      </Button>
                      <Button
                        type="primary"
                        htmlType="submit"
                        className="login-form-button"
                        onClick={() => setSubmitButton(1)}
                      >
                        Prikaži spremljeni filter
                      </Button>
                    </>
                  ) : null}
                </Form.Item>
              </Form>
            </Panel>
          </Collapse>

          <br />

          {postsObjects}

          <br />
        </div>
      </Content>
      <Footer style={{ textAlign: "center" }}>
        PiccoloGrupa © 2020 FER PI
      </Footer>
    </Layout>
  );
}
