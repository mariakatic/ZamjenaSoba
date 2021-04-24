import React from "react";
import {
  Button,
  Card,
  Form,
  Select,
  Typography,
  Input,
  notification,
} from "antd";
import { HomeOutlined } from "@ant-design/icons";
import axios from "axios";

const { Title } = Typography;
const { Option } = Select;

class NewPostComponent extends React.Component {
  token = this.props.token;
  constructor(props) {
    super(props);
    this.state = { gradovi: [], domovi: [] };
  }

  componentDidMount() {
    var gradovi = [];
    axios.get(`${process.env.REACT_APP_API_URL}/api/v1/grad`).then((res) => {
      gradovi = res.data;
      this.setState({ gradovi: gradovi });
    });

    var domovi = [];
    axios.get(`${process.env.REACT_APP_API_URL}/api/v1/dom`).then((res) => {
      domovi = res.data;
      this.setState({ domovi: domovi });
    });
  }

  render() {
    const onFinish = (values) => {
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

    const onFinishFailed = (errorInfo) => {};

    let optionGradovi = this.state.gradovi.map((grad) => (
      <Option value={grad.idGrada}>{grad.imeGrada}</Option>
    ));

    let optionDomovi = this.state.domovi.map((dom) => (
      <Option value={dom.idDom}>{dom.imeDoma}</Option>
    ));

    return (
      <Card>
        <Title>Novi oglas</Title>
        <Title level={4}>Opis sobe</Title>
        <Form
          name="newPost"
          onFinish={onFinish}
          onFinishFailed={onFinishFailed}
        >
          {/* <Form.Item
            name="grad"
            rules={[{ required: true, message: "Odaberite grad!" }]}
          >
            <Select placeholder="Grad">{optionGradovi}</Select>
          </Form.Item> */}

          <Form.Item
            name="domID"
            rules={[{ required: true, message: "Odaberite dom!" }]}
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
              Predaj oglas
            </Button>
          </Form.Item>
        </Form>
      </Card>
    );
  }
}

export default NewPostComponent;
