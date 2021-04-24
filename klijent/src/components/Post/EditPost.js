import React from "react";
import { Form, Input, Button, Drawer, Select, notification } from "antd";
import { HomeOutlined } from "@ant-design/icons";
import axios from "axios";

import "./EditWorkerDrawerCssFix.css";
const { Option } = Select;

class EditPost extends React.Component {
  constructor(props) {
    super(props);
    this.state = { domovi: [], post: null };
  }

  componentDidMount() {
    var domovi = [];
    axios.get(`${process.env.REACT_APP_API_URL}/api/v1/dom`).then((res) => {
      domovi = res.data;
      this.setState({ domovi: domovi });
    });
  }

  validateMessages = () => {};

  render() {
    const openNotification = (status) => {
      if (status === 1) {
        notification.open({
          message: "Uspješna predaja",
          description: "Novi oglas je uspješno predan!",
        });
      } else if (status === 0) {
        notification.open({
          message: "Neuspješna predaja",
          description: "Molimo pokušajte ponovno.",
        });
      }
    };

    const onFinish = (values) => {
      values.idOglas = this.props.idOglas;
      axios
        .post(`${process.env.REACT_APP_API_URL}/api/v1/post`, values, {
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

    const layout = {
      labelCol: { span: 8 },
    };
    let optionDomovi = this.state.domovi.map((dom) => (
      <Option value={dom.idDom}>{dom.imeDoma}</Option>
    ));

    return (
      <div>
        <Drawer
          title="Uredi oglas"
          onClose={() => this.props.closeHandler()}
          visible={this.props.visible}
          bodyStyle={{ paddingBottom: 80 }}
          // style={{ width: "100vw" }}
          width={"400px"}
        >
          <Form
            {...layout}
            name="nest-messages"
            onFinish={onFinish}
            validateMessages={this.validateMessages}
          >
            <Form.Item
              name="domID"
              rules={[{ required: true, message: "Odaberite dom!" }]}
              initialValue={this.props.idDom}
            >
              <Select placeholder="Dom">{optionDomovi}</Select>
            </Form.Item>

            <Form.Item
              name="paviljon"
              rules={[
                { required: true, message: "Odaberite paviljon!" },
                ({ getFieldValue }) => ({
                  validator(rule, value) {
                    const re = /^[0-9\b]+$/;
                    if (
                      value === "" ||
                      (re.test(value) && value >= 1 && value <= 20)
                    ) {
                      return Promise.resolve();
                    }
                    return Promise.reject("Upišite broj od 1 do 20!");
                  },
                }),
              ]}
              initialValue={this.props.paviljon}
            >
              <Input
                prefix={<HomeOutlined className="site-form-item-icon" />}
                placeholder="Paviljon"
              />
            </Form.Item>

            <Form.Item
              name="kategorijaSobe"
              rules={[
                { required: true, message: "Odaberite kategoriju sobe!" },
                ({ getFieldValue }) => ({
                  validator(rule, value) {
                    const re = /^[0-9\b]+$/;
                    if (
                      value === "" ||
                      (re.test(value) && value >= 1 && value <= 7)
                    ) {
                      return Promise.resolve();
                    }
                    return Promise.reject("Upišite broj od 1 do 7!");
                  },
                }),
              ]}
              initialValue={this.props.kategorijaSobe}
            >
              <Input
                prefix={<HomeOutlined className="site-form-item-icon" />}
                placeholder="Kategorija sobe"
              />
            </Form.Item>

            <Form.Item
              name="kat"
              rules={[
                { required: true, message: "Odaberite kat!" },
                ({ getFieldValue }) => ({
                  validator(rule, value) {
                    const re = /^[0-9\b]+$/;
                    if (
                      value === "" ||
                      (re.test(value) && value >= 1 && value <= 10)
                    ) {
                      return Promise.resolve();
                    }
                    return Promise.reject("Upišite broj od 1 do 10!");
                  },
                }),
              ]}
              initialValue={this.props.kat}
            >
              <Input
                prefix={<HomeOutlined className="site-form-item-icon" />}
                placeholder="Kat"
              />
            </Form.Item>

            <Form.Item>
              <Button
                type="primary"
                htmlType="submit"
                className="login-form-button"
              >
                Spremi promjene
              </Button>
            </Form.Item>
          </Form>
        </Drawer>
      </div>
    );
  }
}

export default EditPost;
