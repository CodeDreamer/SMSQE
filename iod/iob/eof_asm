; Buffering (set eof) utility  V2.00    1989  Tony Tebby   QJUMP

        section iou

        xdef    iob_eof

        include 'dev8_keys_buf'

;+++
; This routine sets eof in the queue / buffer header.
;
; This routine can be called from an interrupt server.
;
; This is a clean routine.
;
;       a2 c  p pointer to buffer header
;       all other registers preserved
;
;       status Z if not already EOF, NZ if already EOF.
;--
iob_eof
        tas     buf_eoff(a2)
        rts
        end
