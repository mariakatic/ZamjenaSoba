import React from "react";
import { Form, Input, Button, Drawer, Select, notification } from "antd";
import { UserOutlined, LockOutlined } from "@ant-design/icons";
import axios from "axios";

import "./Post/EditWorkerDrawerCssFix.css";
import Title from "antd/lib/skeleton/Title";
const { Option } = Select;

class EditPost extends React.Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  componentDidMount() {}

  validateMessages = () => {};

  render() {
    const openNotification = (status) => {
      if (status === 1) {
        notification.open({
          message: "Uspješno ažuriranje",
          description: "Novi podatci su uspješno predani!",
        });
      } else if (status === 0) {
        notification.open({
          message: "Neuspješna predaja",
          description: "Molimo pokušajte ponovno.",
        });
      }
    };

    const onFinish = (values) => {
      values.id = this.props.id;
      values.password = "12345678";
      axios
        .post(`${process.env.REACT_APP_API_URL}/api/v1/currentuser`, values, {
          headers: {
            Authorization: "Bearer " + this.props.token, //the token is a variable which holds the token
          },
        })
        .then((res) => {
          openNotification(1);
        })
        .catch((err) => {
          openNotification(0);
        });
    };

    const onFinishPw = (values) => {
      values.id = this.props.id;
      values.username = this.props.username;
      values.name = this.props.name;
      values.email = this.props.email;
      values.jmbag = this.props.jmbag;

      axios
        .post(
          `${process.env.REACT_APP_API_URL}/api/v1/changePassword`,
          values,
          {
            headers: {
              Authorization: "Bearer " + this.props.token, //the token is a variable which holds the token
            },
          }
        )
        .then((res) => {
          openNotification(1);
        })
        .catch((err) => {
          openNotification(0);
        });
    };

    const onFinishFailed = () => {};

    const layout = {
      labelCol: { span: 8 },
    };

    return (
      <div>
        <Drawer
          title="Uredi korisničke podatke"
          onClose={() => this.props.closeHandler()}
          visible={this.props.visible}
          bodyStyle={{ paddingBottom: 80 }}
          // style={{ width: "100vw" }}
          width={"400px"}
        >
          <>
            <Form
              name="normal_login"
              className="login-form"
              onFinish={onFinish}
              onFinishFailed={onFinishFailed}
            >
              <Form.Item
                name="username"
                initialValue={this.props.username}
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
                initialValue={this.props.email}
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
                initialValue={this.props.jmbag}
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
                initialValue={this.props.name}
                rules={[{ required: true, message: "Upiši svoje ime!" }]}
              >
                <Input
                  prefix={<UserOutlined className="site-form-item-icon" />}
                  placeholder="Ime i prezime"
                />
              </Form.Item>

              <Form.Item>
                <Button
                  type="primary"
                  htmlType="submit"
                  className="login-form-button"
                >
                  Ažuriraj podatke
                </Button>
              </Form.Item>
            </Form>
            <Title level={3}>Ažuriraj lozinku</Title>
            <Form
              name="normal_login"
              className="login-form"
              onFinish={onFinishPw}
            >
              <Form.Item
                name="password"
                rules={[
                  { required: false, message: "Upiši svoju lozinku!" },
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
                  { required: false, message: "Potvrdi svoju lozinku!" },
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
                  Ažuriraj lozinku
                </Button>
              </Form.Item>
            </Form>
          </>
        </Drawer>
      </div>
    );
  }
}

export default EditPost;
