package com.pje.kelompok4.controller;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import com.pje.kelompok4.model.Subagian;
import com.pje.kelompok4.payload.request.SubagianReq;
import com.pje.kelompok4.payload.request.SubagianUpdateReq;
import com.pje.kelompok4.payload.response.ResponseDto;
import com.pje.kelompok4.repositoriy.BagianRepo;
import com.pje.kelompok4.repositoriy.SubagianRepo;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/subagian")
public class SubagianController {
    @Autowired
    BagianRepo bagianRepo;

	@Autowired
	SubagianRepo subagianRepo;

    @PostMapping("/create")
	public ResponseEntity<?> createSubagian(@Valid @RequestBody SubagianReq subagianReq) {
		int code   = 201;
		String msg = "subagian baru berhasil dibuat";
		
		try {
			if (subagianRepo.existsByName(subagianReq.getName())) {
				code = 400;
				msg  = "nama subagian ini sudah dipakai"; 
			}
			else if (bagianRepo.existsById(subagianReq.getIdBagian()) == false) {
				code = 400;
				msg  = "id bagian {"+subagianReq.getIdBagian()+"} tidak terdaftar";
			}
	
			if (code < 300) {
				Subagian subagian = new Subagian();
				Date date         = new Date();
				String strDate    = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss").format(date);
				
				subagian.setName(subagianReq.getName());
				subagian.setDescription(subagianReq.getDescription());
				subagian.setIdBagian(subagianReq.getIdBagian());
				subagian.setCreatedAt(strDate);
				subagianRepo.save(subagian);
			}
        } 
        catch (Exception e) {
			code = 500;
			msg  = e.getMessage();
        }

		return ResponseEntity
			.status(code)
			.body(new ResponseDto<>(
				code,
				(code < 300) ? "success" : "failed",
				msg
			));
	}

    @PutMapping("/update")
	public ResponseEntity<?> updateSubagian(@Valid @RequestBody SubagianUpdateReq sbUpdateReq) {
		int code   = 201;
		String msg = "subagian berhasil diedit";

		try {
			// Check id is exist
			if (subagianRepo.existsById(sbUpdateReq.getId()) == false) {
				code = 400;
				msg  = "subagian dengan id {"+sbUpdateReq.getId()+"} tidak ditemukan"; 
			}
			else {
				// Check name is exist
				Subagian result = subagianRepo.validateName(sbUpdateReq.getName(), sbUpdateReq.getId());
				
				if (Objects.isNull(result) == false) {
					code = 400;
					msg  = "nama subagian ini sudah dipakai";
				}
				// Check id_bagian is exist
				else if (bagianRepo.existsById(sbUpdateReq.getIdBagian()) == false) {
					code = 400;
					msg  = "id bagian {"+sbUpdateReq.getIdBagian()+"} tidak terdaftar";
				}
				else {
					Subagian subagian = subagianRepo.findSubagianById(sbUpdateReq.getId());
					Date date         = new Date();
					String strDate    = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss").format(date);
					
					subagian.setName(sbUpdateReq.getName());
					subagian.setDescription(sbUpdateReq.getDescription());
					subagian.setIdBagian(sbUpdateReq.getIdBagian());
					subagian.setUpdatedAt(strDate);
					subagianRepo.save(subagian);
				}
			}
        } 
        catch (Exception e) {
			code = 500;
			msg  = e.getMessage();
        }

		return ResponseEntity
			.status(code)
			.body(new ResponseDto<>(
				code,
				(code < 300) ? "success" : "failed",
				msg
			));
	}

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSubagian(@PathVariable Long id) {
		int code   = 201;
		String msg = "subagian dengan id {"+id+"} berhasil dihapus";

		try {
			if (subagianRepo.existsById(id) == false) {
				code = 400;
				msg  = "subagian dengan id {"+id+"} tidak ditemukan";
			}
			else {
				subagianRepo.deleteById(id);
			}
        } 
        catch (Exception e) {
			code = 500;
			msg  = e.getMessage();
        }

		return ResponseEntity
			.status(code)
			.body(new ResponseDto<>(
				code,
				(code < 300) ? "success" : "failed",
				msg
			));
    }

    @GetMapping
	public ResponseEntity<?> getSubagian(@RequestParam(required = false) Long id) {
		try {
			if (Objects.isNull(id) == false) {
				Subagian subagian = subagianRepo.findSubagianById(id);
		
				ResponseDto<?> responseData = new ResponseDto<>(
					(Objects.isNull(subagian)) ? 404 : 200,
					(Objects.isNull(subagian)) ? "failed" : "success",
					(Objects.isNull(subagian)) ? "subagian dengan id {"+id+"} tidak ditemukan" : subagian
				);
				
				return ResponseEntity
					.status(HttpStatus.OK)
					.body(responseData);
			} 
			else {
				Iterable<Subagian> listSubagian = subagianRepo.findAll();
		
				ResponseDto<?> responseData = new ResponseDto<>(
					200,
					"success",
					listSubagian
				);
	
				return ResponseEntity
					.status(HttpStatus.OK)
					.body(responseData);
			}
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
