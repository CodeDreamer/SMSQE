; Buffering (get line with parity) utility  V2.00    1989  Tony Tebby   QJUMP

        section iou

        xdef    iob_glpr

        xref    iob_gbpr

        include 'dev8_keys_buf'
        include 'dev8_keys_err'
        include 'dev8_keys_k'

;+++
; This routine gets multiple bytes from an IO buffer. The transfer is
; terminated by a <NL> <EOF> or if the transfer is complete. If it needs a
; new buffer, the old buffer is added to the linked list of throwaways.
;
; If it is end of file, the buffer is thrown away and A2 is returned as
; either the next list of buffers or zero. The end of file flag is put in
; (A1), but neither D1 nor A1 is updated.
;
; When A2 is modified, the corresponding current output queue pointer is
; updated.
;
; This routine can be called from an interrupt server.
;
; This is a clean routine.
;
;       d0  r   status: 0, err.nc, err.prty, err.bffl or +1 for end of file
;       d1 c  u byte count
;       d2 c  p number of bytes to transfer
;       d7 c  p byte parity code
;       a1 c  u destination for bytes transferred
;       a2 c  u pointer to buffer header, updated if new buffer or eof
;       a6 c  p pointer to system variables
;       all other registers preserved
;--
iob_glpr
        moveq   #0,d0
        cmp.l   d1,d2                    ; number of bytes to read
        ble.s   ibgm_bffl
        move.l  d1,-(sp)
        jsr     iob_gbpr
        bne.s   ibgm_bad
        move.b  d1,(a1)
        move.l  (sp)+,d1
        addq.l  #1,d1
        cmp.b   #k.nl,(a1)+              ; newline at end
        bne.s   iob_glpr
        bra.s   ibgm_done
ibgm_bad
        blt.s   ibgm_rest
        move.b  d1,(a1)
ibgm_rest
        move.l  (sp)+,d1
ibgm_done
        tst.l   d0
        rts
ibgm_bffl
        moveq   #err.bffl,d0
        bra.s   ibgm_done
        end
