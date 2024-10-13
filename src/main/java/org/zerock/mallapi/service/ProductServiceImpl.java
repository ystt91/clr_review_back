package org.zerock.mallapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.mallapi.domain.Product;
import org.zerock.mallapi.domain.ProductImage;
import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.ProductDTO;
import org.zerock.mallapi.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage()-1,pageRequestDTO.getSize(), Sort.by("pno").descending());

        Page<Object[]> result = productRepository.selectList(pageable);

        //Object[] = 0 product 1 productImage
        //Object[] = 0 product 1 productImage
        //Object[] = 0 product 1 productImage 여러개

        List<ProductDTO> dtoList = result.get().map(arr -> {

            ProductDTO productDTO = null;

            //projection을 쓰는 방법도 있다고 한다 뭐지?

            Product product = (Product)arr[0];
            ProductImage productImage = (ProductImage)arr[1];

            productDTO=ProductDTO.builder()
                    .pno(product.getPno())
                    .pname(product.getPname())
                    .pdesc(product.getPdesc())
                    .price(product.getPrice())
                    .build();


            if (productImage != null) { // productImage가 null이 아닐 경우에만 파일 이름을 설정합니다.
                String imageStr = productImage.getFileName();
                productDTO.setUploadFileNames(List.of(imageStr));
            } else {
                productDTO.setUploadFileNames(List.of()); // productImage가 null일 경우 빈 리스트로 설정합니다.
            }

            return productDTO;
        }).collect(Collectors.toList());

        long totalCount = result.getTotalElements();

        return PageResponseDTO.<ProductDTO>withAll()
                .dtoList(dtoList)
                .total(totalCount)
                .pageRequestDTO(pageRequestDTO)
                .build();

    }

    @Override
    public Long register(ProductDTO productDTO) {

        Product product = dtoToEntity(productDTO);

        log.info("---------------------------");
        log.info(product);
        log.info(product.getImageList());

        Long pno = productRepository.save(product).getPno();

        return pno;
    }

    @Override
    public ProductDTO get(Long pno) {

        Optional<Product> result = productRepository.findById(pno);

        Product product = result.orElseThrow();

        ProductDTO productDTO = entityToDTO(product);

        return productDTO;

    }

    @Override
    public void modify(ProductDTO productDTO) {

        // 조회
        Optional<Product> result = productRepository.findById(productDTO.getPno());

        Product product = result.orElseThrow();

        // 변경 내용 반영
        product.changeName(productDTO.getPname());
        product.changeDesc(productDTO.getPdesc());
        product.changePrice(productDTO.getPrice());
        product.changeDel(productDTO.isDelFlag());

        //이미지 처리
        List<String> uploadFileNames = productDTO.getUploadFileNames();

        // JPA가 관리하는 한 객체에서만 변경을 해주어야 하기 때문에 전체 삭제
        product.clearList();

        // uploadFileNames 리스트의 각 요소가 uploadFileName 변수로 전달
        if(uploadFileNames != null && !uploadFileNames.isEmpty()) {
            uploadFileNames.forEach(uploadFileName -> {
                product.addImageString(uploadFileName);
            });
        }

        // 저장
        productRepository.save(product);
    }

    @Override
    public void remove(Long pno) {
        productRepository.deleteById(pno);
    }

    private ProductDTO entityToDTO(Product product) {
        ProductDTO productDTO = ProductDTO.builder()
                .pno(product.getPno())
                .pname(product.getPname())
                .pdesc(product.getPdesc())
                .price(product.getPrice())
                .delFlag(product.isDelFlag())
                .build();

        List<ProductImage> imageList = product.getImageList();

        if(imageList == null || imageList.size() == 0){
            return productDTO;
        }

        List<String> fileNameList =
                imageList.stream().map(productImage -> productImage.getFileName()).toList();

        productDTO.setUploadFileNames(fileNameList);

        return productDTO;

    }

    private Product dtoToEntity(ProductDTO productDTO) {

        Product product = Product.builder()
                .pno(productDTO.getPno())
                .pname(productDTO.getPname())
                .pdesc(productDTO.getPdesc())
                .price(productDTO.getPrice())
                .build();

        List<String> uploadFileNames = productDTO.getUploadFileNames();

        if(uploadFileNames == null || uploadFileNames.size() == 0) {
            return product;
        }

        uploadFileNames.forEach(fileName -> {
            product.addImageString(fileName);
        });

        return product;

    }



}
