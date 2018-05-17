; Direct Sector IO operations   V2.00     1987  Tony Tebby   QJUMP

        section iou

        xdef    iou_sect

        xref    iou_ckro

        include 'dev8_keys_chn'
        include 'dev8_keys_iod'
        include 'dev8_keys_err'
        include 'dev8_keys_qdos_io'
;+++
; Direct Sector IO routines
;
;       d0 c    operation
;       d1 cr   amount transferred / byte / position etc.
;       d2 c    buffer size
;       d5   s  file pointer
;       d6 c  p drive number / sector length
;       a0 c    channel base address
;       a1 cr   buffer address
;       a2   s
;       a3 c  p linkage block address
;       a4 c  p pointer to physical definition
;---
iou_sect
ios.off equ     $60
        subq.b  #iob.fmul,d0             ; is it fetch multiple?
        beq.s   ios_read
        subq.b  #iob.smul-iob.fmul,d0    ; is it send multiple?
        beq.s   ios_write
        sub.b   #iof.posa-iob.smul,d0    ; is it position?
        beq.s   ios_posab
        subq.b  #iof.posr-iof.posa,d0    ; relative?
        beq.s   ios_posre
        moveq   #err.ipar,d0             ; ... no
        rts

; read a sector

ios_read
        move.l  a1,-(sp)                 ; save pointer
        moveq   #iod_rsec-ios.off,d7
        bclr    #1,d2                    ; is there a word length at the start?
        beq.s   ios_length

        clr.w   (a1)
        bset    d6,(a1)                  ; set length * 2
        lsr.w   (a1)+                    ; set length
        bra.s   ios_length

; write a sector

ios_write
        jsr     iou_ckro                 ; read only?

        move.l  a1,-(sp)                 ; save pointer
        move.b  #iodd.mod,iod_drst(a4)   ; set status modified

        moveq   #iod_wsec-ios.off,d7
        bclr    #1,d2                    ; is there a word length at the start?
        beq.s   ios_length
        addq.l  #2,a1                    ; skip it
ios_length
        moveq   #0,d0
        tst.w   d2                       ; was it just length?
        beq.s   ios_a1

; set up for read/write

        move.l  chn_fpos(a0),d0          ; position
        jsr     ios.off(a3,d7.w)         ; do it
ios_a1
        move.l  a1,d1                    ; set d1 to difference in a1
        sub.l   (sp)+,d1
        tst.l   d0
        rts

; set the file position

ios_posab
        move.l  d1,chn_fpos(a0)          ; set position
ios_posre
        move.l  chn_fpos(a0),d1          ; read position
        moveq   #0,d0
        rts
        end
