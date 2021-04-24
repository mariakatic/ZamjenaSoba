import React from "react";
import { Card, Typography } from "antd";

const { Meta } = Card;
const { Title } = Typography;

function MyRoom(props) {
  const id = props.id;
  const kat = props.kategorija;
  const kategorija = props.kategorija;
  const paviljon = props.paviljon;
  const id_dom = props.id_dom;
  const id_student = props.id_student;

  return (
    <Card>
      <Title>Moja soba</Title>
      <p>ID: {id}</p>
      <p>Kat: {kat}</p>
      <p>Kategorija: {kategorija}</p>
      <p>Paviljon: {paviljon}</p>
      <p>ID doma: {id_dom}</p>
      <p>ID studenta: {id_student}</p>
    </Card>
  );
}

export default MyRoom;
