; Buffering (get multi-byte) utility  V2.00    1989  Tony Tebby   QJUMP

        section iou

        xdef    iob_gmul

        xref    iob_gbyt

        include 'dev8_keys_buf'

;+++
; This routine gets multiple bytes from an IO buffer. If it needs a
; new buffer, the old buffer is added to the linked list of throwaways.
;
; If it is end of file, the buffer is thrown away and A2 is returned as
; either the next list of buffers or zero. The end of file flag is put in
; (A1), but neither D1 nor A1 are incremented.
;
; When A2 is modified, the corresponding current output queue pointer is
; updated.
;
; This routine can be called from an interrupt server.
;
; This is a clean routine.
;
;       d0  r   status: 0, err.nc or +1 for end of file
;       d1 c  u byte count
;       d2 c  p number of bytes to transfer
;       a1 c  u destination for bytes transferred
;       a2 c  u pointer to buffer header, updated if new buffer or eof
;       a6 c  p pointer to system variables
;       all other registers preserved
;--
iob_gmul
        moveq   #0,d0
        cmp.l   d1,d2                    ; number of bytes to read
        ble.s   ibgm_done
        move.l  d1,-(sp)
        jsr     iob_gbyt
        bne.s   ibgm_bad
        move.b  d1,(a1)+
        move.l  (sp)+,d1
        addq.l  #1,d1
        bra.s   iob_gmul
ibgm_bad
        blt.s   ibgm_rest
        move.b  d1,(a1)                  ; set eoff
ibgm_rest
        move.l  (sp)+,d1
ibgm_done
        tst.l   d0
        rts
        end
