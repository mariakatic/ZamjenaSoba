import React, { useContext, useState, useRef, useEffect } from "react";
import { Layout, Typography, message, Card, notification } from "antd";
import { Form, Input, Button } from "antd";
import NavBar from "../components/NavBar/NavBar";
import {
  UserOutlined,
  LockOutlined,
  StepForwardOutlined,
} from "@ant-design/icons";
import axios from "axios";
import { Redirect } from "react-router-dom";

// import { LoginContext } from "../Context";
// import { TokenContext } from "../Context";
// import { RoleContext } from "../Context";
import { InfoContext } from "../Context";

const { Content, Footer } = Layout;
const { Title } = Typography;

//ova funkcija samo sluzi za ne imat memoryleak
function useStateSafely(initialValue) {
  const [value, setValue] = useState(initialValue);
  const isSafe = useRef(true);

  useEffect(() => {
    return () => {
      isSafe.current = false;
    };
  }, []);

  const setValueSafely = (newValue) => {
    if (isSafe.current) {
      setValue(newValue);
    }
  };

  return [value, setValueSafely];
}

export default function LoginPage() {
  const { info, setInfo } = useContext(InfoContext);
  // const { loggedIn, setLoggedIn } = useContext(LoginContext);
  // const { token, setToken } = useContext(TokenContext);
  // const [role, setRole] = useStateSafely("");
  // const { role, setRole } = useContext(RoleContext);

  const onFinishFailed = (errorInfo) => {};

  const onFinish = (values) => {
    const openNotification = (status) => {
      if (status === 1) {
        notification.open({
          message: "Prijava uspješna",
          description: "Prebacujemo vas",
        });
        setInfo({loggedIn: true});
      } else if (status === 0) {
        notification.open({
          message: "Prijava neuspješna",
          description: "Molimo pokušajte ponovno",
        });
      }
    };

    axios
      .post(`${process.env.REACT_APP_API_URL}/api/v1/signin`, values)
      .then((res) => {
        let token = res.data.accessToken;
        setInfo({token:token});
        getRole(res.data.accessToken);
        console.log(res.data.role);
        // setRole(res.data.role);
        openNotification(1);
      })
      .catch((res) => {
        openNotification(0);
      });
  };

  const getRole = (token2) => {
    axios
      .get(`${process.env.REACT_APP_API_URL}/api/v1/currentuser`, {
        headers: {
          Authorization: "Bearer " + token2, //the token is a variable which holds the token
        },
      })
      .then((res) => {
        console.log("new role " + res.data.userRole);
        setInfo({role:res.data.userRole});
        console.log("role variable: " + info.role);
      });
  };

  useEffect(() => {
    if (info.token) {
      getRole(info.token);
    }
  }, []);

  return (
    <React.Fragment>
      {info.loggedIn && info.role === "SC_DJELATNIK" ? (
        <Redirect to="/scdjelatnik" />
      ) : null}
      {info.loggedIn && info.role === "STUDENT" ? <Redirect to="/" /> : null}
      <Layout className="layout" style={{ minHeight: "100vh" }}>
        <NavBar />
        <Content style={{ maxWidth: "720px", margin: "auto", padding: "2em" }}>
          <Card>
            <Title>Prijava</Title>
            <Form
              name="normal_login"
              className="login-form"
              onFinish={onFinish}
              onFinishFailed={onFinishFailed}
            >
              <Form.Item
                name="username"
                rules={[
                  { required: true, message: "Upiši svoje korisničko ime!" },
                ]}
              >
                <Input
                  prefix={<UserOutlined className="site-form-item-icon" />}
                  placeholder="Korisničko ime"
                />
              </Form.Item>
              <Form.Item
                name="password"
                rules={[{ required: true, message: "Upiši svoju lozinku!" }]}
              >
                <Input
                  prefix={<LockOutlined className="site-form-item-icon" />}
                  type="password"
                  placeholder="Lozinka"
                />
              </Form.Item>

              <Form.Item>
                <Button
                  type="primary"
                  htmlType="submit"
                  className="login-form-button"
                >
                  Prijavi se
                </Button>
              </Form.Item>
            </Form>
          </Card>
        </Content>
        <Footer style={{ textAlign: "center" }}>
          PiccoloGrupa © 2020 FER PI
        </Footer>
      </Layout>
    </React.Fragment>
  );
}
