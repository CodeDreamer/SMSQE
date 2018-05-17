; Buffering (test byte) utility  V2.00    1989  Tony Tebby   QJUMP

        section iou

        xdef    iob_tbpr

        xref    iob_tbyt
        xref    iob_prtc

        include 'dev8_keys_buf'
        include 'dev8_keys_err'

;+++
; This routine tests for the presence of a byte in an IO buffer. It then
; checks the parity.
;
; This routine can be called from an interrupt server.
;
; This is a clean routine.
;
;       d0  r   status: 0, err.nc or +1 for end of file
;       d1  r   next byte
;       d7 c  p byte parity
;       a2 c  p pointer to buffer header
;       all other registers preserved
;--
iob_tbpr
        jsr     iob_tbyt                 ; test byte
        bne.s   iob_rts
        move.b  d7,d0                    ; parity?
        bne.l   iob_prtc                 ; parity check
iob_rts
        rts
        end
