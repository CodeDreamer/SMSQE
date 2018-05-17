; Buffering (free buffer) utility  V2.00    1989  Tony Tebby   QJUMP

        section iou

        xdef    iob_fbuf
        xdef    iob_fbfl

        include 'dev8_keys_buf'
        include 'dev8_keys_sys'
        include 'dev8_keys_qdos_sms'
        include 'dev8_keys_68000'

;+++
; This routine frees the current buffer, by adding it to the scheduler list,
; and transfers the next buffer list link to the new buffer
;
; This routine can be called from an interrupt server.
;
; This is a clean routine.
;
;       a2 c  u pointer to buffer header, returned = a3
;       a3 c  p pointer to new buffer
;       a6 c  p pointer to system variables
;       all other registers including D0 preserved
;--
iob_fbfl
        move.l  buf_nxtl(a2),buf_nxtl(a3)
;+++
; This routine frees the current buffer by adding it to the scheduler list.
; First it checks if the pointer to pointer to get buffer is set. If not
; it does not throw the buffer away.
;
; This routine can be called from an interrupt server.
;
; This is a clean routine.
;
;       a2 c  u pointer to buffer header, returned = a3
;       a3 c  p pointer to new buffer
;       a6 c  p pointer to system variables
;       all other registers including D0 preserved
;       status return arbitrary
;--
iob_fbuf
        movem.l d0/d1/d2/a0,-(sp)
        tst.l   buf_ptrg(a2)             ; any get pointer?
        beq.s   iob_nop                  ; ... no

        moveq   #sms.info,d0             ; find system vars
        trap    #do.sms2
        move.w  sr,-(sp)
        or.w    #sr.i7,sr                ; very atomic for this
        move.l  sys_frbl(a0),(a2)        ; free the buffer sometime
        move.l  a2,sys_frbl(a0)
        move.l  buf_ptrg(a2),a2          ; set get buffer pointer
        move.l  a3,(a2)                  ; ... new pointer
        move.w  (sp)+,sr
iob_nop
        move.l  a3,a2                    ; return new pointer
        movem.l (sp)+,d0/d1/d2/a0
        rts
        end
