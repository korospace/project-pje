package com.pje.kelompok4.controller;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import com.pje.kelompok4.model.Bagian;
import com.pje.kelompok4.payload.request.BagianReq;
import com.pje.kelompok4.payload.request.BagianUpdateReq;
import com.pje.kelompok4.payload.response.ResponseDto;
import com.pje.kelompok4.repositoriy.BagianRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/bagian")
public class BagianController {
    @Autowired
    BagianRepo bagianRepo;

    @PostMapping("/create")
	public ResponseEntity<?> createBagian(@Valid @RequestBody BagianReq bagianReq) {
		try {
			if (bagianRepo.existsByName(bagianReq.getName())) {
				ResponseDto<?> responseData = new ResponseDto<>(
					400,
					"failed",
					"nama bagian sudah dipakai!"
				);

				return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(responseData);
			}
	
			Bagian bagian  = new Bagian();
			Date date      = new Date();
			String strDate = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss").format(date);

            bagian.setName(bagianReq.getName());
            bagian.setDescription(bagianReq.getDescription());
			bagian.setCreatedAt(strDate);
			bagianRepo.save(bagian);
	
			ResponseDto<?> responseData = new ResponseDto<>(
				201,
				"success",
				"bagian baru berhasil dibuat!"
			);

			return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(responseData);
        } 
        catch (Exception e) {
            ResponseDto<?> responseData = new ResponseDto<>(
				500,
				"failed",
				e.getMessage()
			);

            return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(responseData);
        }
	}

    @PutMapping("/update")
	public ResponseEntity<?> updateBagian(@Valid @RequestBody BagianUpdateReq bUpdateReq) {
		try {
			// Check id is exist
			if (bagianRepo.existsById(bUpdateReq.getId()) == false) {
				ResponseDto<?> responseData = new ResponseDto<>(
					400,
					"failed",
					"bagian dengan id {"+bUpdateReq.getId()+"} tidak ditemukan!"
				);

				return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(responseData);
			}

			// Check name is exist
			Bagian result = bagianRepo.validateName(bUpdateReq.getName(), bUpdateReq.getId());

			if (Objects.isNull(result) == false) {
				ResponseDto<?> responseData = new ResponseDto<>(
					400,
					"failed",
					"nama bagian ini sudah dipakai!"
				);

				return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(responseData);
			}
	
			Bagian bagian  = bagianRepo.findBagianById(bUpdateReq.getId());
			Date date      = new Date();
			String strDate = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss").format(date);
			
            bagian.setName(bUpdateReq.getName());
            bagian.setDescription(bUpdateReq.getDescription());
			bagian.setUpdatedAt(strDate);
			bagianRepo.save(bagian);
				
			ResponseDto<?> responseData = new ResponseDto<>(
				201,
				"success",
				// updateUserReq
                "bagian dengan id {"+bUpdateReq.getId()+"} berihasil diedit!"
			);

			return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(responseData);
        } 
        catch (Exception e) {
            ResponseDto<?> responseData = new ResponseDto<>(
				500,
				"failed",
				e.getMessage()
			);

            return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(responseData);
        }
	}

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBagian(@PathVariable Long id) {
        try {
            if (bagianRepo.existsById(id) == false) {
				ResponseDto<?> responseData = new ResponseDto<>(
					400,
					"failed",
					"bagian dengan id {"+id+"} tidak ditemukan!"
				);

				return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(responseData);
			}

            bagianRepo.deleteById(id);
            
            ResponseDto<?> responseData = new ResponseDto<>(
					200,
					"success",
					"bagian dengan id {"+id+"} berhasil dihapus!"
				);

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseData);
        } 
        catch (Exception e) {
            ResponseDto<?> responseData = new ResponseDto<>(
				500,
				"failed",
				e.getMessage()
			);

            return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(responseData);
        }
    }

    @GetMapping
	public ResponseEntity<?> getBagian() {
		try {
            Iterable<Bagian> listBagian = bagianRepo.findAll();
	
			ResponseDto<?> responseData = new ResponseDto<>(
				200,
				"success",
				listBagian
			);

			return ResponseEntity
				.status(HttpStatus.OK)
				.body(responseData);
        } 
        catch (Exception e) {
            ResponseDto<?> responseData = new ResponseDto<>(
				500,
				"failed",
				e.getMessage()
			);

            return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(responseData);
        }
	}
}
