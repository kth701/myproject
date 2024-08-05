
console.log("/js/reply.js.....")

// ------------------------------------------------------ //
// 특정 게시글에 대한 댓글 조회 : axios(비동기) 요청 테스트
// ------------------------------------------------------ //
 async function getReply(bno){
  console.log("bno:",bno);

  const result = await axios.get(`/replies/list/${bno}`)
  console.log("getReply(): ",result)
  console.log("getReply() data: ",result.data)
  console.log("getReply() data.list: ",result.data.list)
  return result.data;
}
// ------------------------------------------------------ //