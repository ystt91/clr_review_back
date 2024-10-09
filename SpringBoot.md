
# 게시글과 댓글의 관계 @ManyToOne
#  게시물과 댓글의 시간대가 다르다. = 라이프 사이클이 다르다.

# 상품과 상품에 속한 이미지들의 관계는 그렇지 않다.
# @ElementCollection id가 없는 값 타입
# 관리의 주체가 상품이다. 상품의 이미지가 아닌 것임.

# ElementCollection은 주인공이 아니다.
# 상품의 이미지를 수정하는 것 = 상품의 정보를 수정하는 것

#    @Transactional // 이게 없다면? 세션이 2개가 발생해서 에러가 뜬다.
    @Transactional
    @Test
    public void testRead(){
        Long pno = 1L;

        Optional<Product> result = productRepository.findById(pno);

        Product product = result.orElseThrow();

        log.info(product);
        log.info(product.getImageList());
    }

# EntityGraph를 이용해서 DB를 한번만 들려도 되도록 할 것

# JPA의 맹점 : PERSISTENCE CONTEXT가 JPA 객체를 관리해주어야 하기 때문에
#             기존 JPA 객체를 활용하기 위해 clearList()를 해야 함.
    @Test
    public void testUpdate(){
        Product product = productRepository.selectOne(1L).get();
        product.changePrice(3000);
        product.clearList();

        product.addImageString(UUID.randomUUID()+"_" +"pimage1.jpg");
        product.addImageString(UUID.randomUUID()+"_" +"pimage2.jpg");
        product.addImageString(UUID.randomUUID()+"_" +"pimage3.jpg");

        productRepository.save(product);
    }

# @Transactional
거래라는 뜻, 비즈니스 계층에서 사용

# ElementCollection의 장점
Entity만 지우면 엘리먼트 컬렉션은 따라서 지워진다.
