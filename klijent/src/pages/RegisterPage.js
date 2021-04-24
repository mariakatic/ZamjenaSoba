import React, { useState } from "react";
import { Layout, Typography, notification } from "antd";
import { Form, Input, Button, Card } from "antd";
import NavBar from "../components/NavBar/NavBar";
import { UserOutlined, LockOutlined } from "@ant-design/icons";
import axios from "axios";
import { Redirect } from "react-router-dom";

const { Content, Footer } = Layout;
const { Title } = Typography;

export default function RegisterPage() {
  const [registered, setregistered] = useState(false);

  const onFinish = (values) => {
    delete values["confirmPassword"];
    axios
      .post(`${process.env.REACT_APP_API_URL}/api/v1/signup`, values) //saljemo sve valuese osim confirmPassword
      .then((res) => {
        openNotification(1);
        setregistered(true);
      })
      .catch((res) => {
        openNotification(0);
      });
  };

  const onFinishFailed = (errorInfo) => {};

  const openNotification = (status) => {
    if (status === 1) {
      notification.open({
        message: "Registracija uspješna",
        description: "Biti ćete prebačeni na stranicu za prijavu",
      });
    } else if (status === 0) {
      notification.open({
        message: "Registracija neuspješna",
        description: "Molimo pokušajte ponovno",
      });
    }
  };

  return (
    <React.Fragment>
      {registered ? <Redirect to="/login" /> : null}
      <Layout className="layout" style={{ minHeight: "100vh" }}>
        <NavBar />
        <Content style={{ maxWidth: "720px", margin: "auto", padding: "2em" }}>
          <Card>
            <Title>Registracija</Title>
            <Form
              name="normal_login"
              className="login-form"
              onFinish={onFinish}
              onFinishFailed={onFinishFailed}
            >
              <Form.Item
                name="username"
                rules={[
                  {
                    required: true,
                    message: "Upiši jedinstveno korisničko ime!",
                  },
                ]}
              >
                <Input
                  prefix={<UserOutlined className="site-form-item-icon" />}
                  placeholder="Korisničko ime"
                />
              </Form.Item>

              <Form.Item
                name="email"
                rules={[
                  { required: true, message: "Upiši svoj Email!" },
                  { type: "email", message: "Neispravan unos email-a" },
                ]}
              >
                <Input
                  prefix={<UserOutlined className="site-form-item-icon" />}
                  placeholder="Email"
                />
              </Form.Item>

              <Form.Item
                name="jmbag"
                rules={[
                  { required: true, message: "Upiši svoj JMBAG!" },
                  ({ getFieldValue }) => ({
                    validator(rule, value) {
                      const re = /^[0-9\b]+$/;
                      if (value === "" || re.test(value)) {
                        return Promise.resolve();
                      }
                      return Promise.reject("Neispravan unos JMBAG-a!");
                    },
                  }),
                  { min: 10, message: "JMBAG mora sadržavati 10 brojeva!" },
                  { max: 10, message: "JMBAG mora sadržavati 10 brojeva!" },
                ]}
              >
                <Input
                  prefix={<UserOutlined className="site-form-item-icon" />}
                  placeholder="JMBAG"
                />
              </Form.Item>

              <Form.Item
                name="name"
                rules={[{ required: true, message: "Upiši svoje ime!" }]}
              >
                <Input
                  prefix={<UserOutlined className="site-form-item-icon" />}
                  placeholder="Ime i prezime"
                />
              </Form.Item>

              <Form.Item
                name="password"
                rules={[
                  { required: true, message: "Upiši svoju lozinku!" },
                  { min: 8, message: "Potrebno najmanje 8 znakova!" },
                ]}
              >
                <Input
                  prefix={<LockOutlined className="site-form-item-icon" />}
                  type="password"
                  placeholder="Lozinka"
                />
              </Form.Item>

              <Form.Item
                name="confirmPassword"
                rules={[
                  { required: true, message: "Potvrdi svoju lozinku!" },
                  ({ getFieldValue }) => ({
                    validator(rule, value) {
                      if (!value || getFieldValue("password") === value) {
                        return Promise.resolve();
                      }
                      return Promise.reject("Lozinke se ne podudaraju!");
                    },
                  }),
                ]}
              >
                <Input
                  prefix={<LockOutlined className="site-form-item-icon" />}
                  type="password"
                  placeholder="Potvrdi lozinku"
                />
              </Form.Item>

              <Form.Item>
                <Button
                  type="primary"
                  htmlType="submit"
                  className="login-form-button"
                >
                  Registriraj se
                </Button>
              </Form.Item>
            </Form>
          </Card>
        </Content>
        <Footer style={{ textAlign: "center" }}>
          PiccoloGrupa © 2020 FER PI
        </Footer>
        {/* <Button onClick={redirectLogin}>redirect</Button> */}
      </Layout>
    </React.Fragment>
  );
}
