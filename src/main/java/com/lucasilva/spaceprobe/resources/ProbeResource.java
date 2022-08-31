package com.lucasilva.spaceprobe.resources;

import com.lucasilva.spaceprobe.dto.*;
import com.lucasilva.spaceprobe.service.ProbeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/probes")
public class ProbeResource {

    @Autowired
    private ProbeService service;

    @PostMapping
    public ResponseEntity<ProbeResponseDto> createProbe(@RequestBody @Valid ProbeDto probeDto) {
        var probe = service.createProbe(probeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(probe);
    }

    @GetMapping
    public ResponseEntity<List<ProbeResponseDto>> getAllProbes() {
        return ResponseEntity.ok(service.getAllProbes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProbeAllDataDto> getProbeById(@PathVariable String id) {
        return ResponseEntity.ok(service.getProbeAllDataById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProbeUpdateResponseDto> updateProbe(@PathVariable String id,
                                                              @RequestBody @Valid ProbeUpdateDto probeUpdateDto) {
        return ResponseEntity.ok(service.updateProbe(id, probeUpdateDto));
    }
}
