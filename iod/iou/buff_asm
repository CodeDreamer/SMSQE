; IO buffering   V2.02     1987  Tony Tebby   QJUMP

        section iou

        xdef    iou_fmul
        xdef    iou_fmlw
        xdef    iou_flin
        xdef    iou_smul
        xdef    iou_smlw

        xdef    iou_fden
        xdef    iou_fde0
        xdef    iou_sden
        xdef    iou_sde0

        include 'dev8_keys_err'
        include 'dev8_keys_k'
        include 'dev8_keys_iod'
        include 'dev8_keys_hdr'
        include 'dev8_keys_chn'

;+++
; Fetch directory entry (atomic) d7 bytes starting at beginning of entry.
; CHN_SDPS and CHN_SDID must be valid.
;
;       d1  u   byte count
;       d6 c  p drive number / (file id)
;       d7 cr   amount to buffer / unbuffer
;       a0 c  p pointer to channel block
;       a1 cr   buffer address
;       a2   s  internal buffer address
;       a3 c  p linkage block address
;       a4 c  p pointer to physical definition
;       a5 c  p pointer to map
;
;       status return standard
;---
iou_fde0
        movem.l d5/d6,-(sp)
        move.l  chn_sdps(a0),d5          ; start
        bra.s   ifdw_do

;+++
; Fetch directory entry (atomic) d7 bytes starting at byte d4 of entry.
; CHN_SDPS and CHN_SDID must be valid.
;
;       d1  u   byte count
;       d4 c  p byte position (long)
;       d6 c  p drive number / (file id)
;       d7 cr   amount to buffer / unbuffer
;       a0 c  p pointer to channel block
;       a1 cr   buffer address
;       a2   s  internal buffer address
;       a3 c  p linkage block address
;       a4 c  p pointer to physical definition
;       a5 c  p pointer to map
;
;       status return standard
;---
iou_fden
        movem.l d5/d6,-(sp)
        move.l  chn_sdps(a0),d5          ; start
        add.l   d4,d5
ifdw_do
        move.w  chn_sdid(a0),d6          ; sub-directory
        bsr.s   ifw_do
        movem.l (sp)+,d5/d6
        rts

;+++
; Fetch multiple bytes and wait, d7 bytes starting at byte d5
;
;       d1  u   byte count
;       d4    p block / byte buffer position
;       d5 cr   file position
;       d6 c  p drive number / sub-directory or file id
;       d7 cr   amount to buffer / unbuffer
;       a0 c  p pointer to channel block
;       a1 cr   buffer address
;       a2   s  internal buffer address
;       a3 c  p linkage block address
;       a4 c  p pointer to physical definition
;       a5 c  p pointer to map
;
;       status return standard
;---
iou_fmlw
ifw_do
        movem.l d3/d4,-(sp)
ifw_retry
        bsr.s   iou_fmul                 ; fetch multiple bytes
        cmp.w   #err.nc,d0               ; is it not complete?
        beq.s   ifw_retry                ; ... yes (or no??)
        movem.l (sp)+,d3/d4
        tst.l   d0
        rts
;+++
; Fetch d7 bytes from file d6 starting at byte d5
;
;       d1  u   byte count
;       d3   s  amount to be read
;       d4   s  block / byte buffer position
;       d5 cr   file position
;       d6 c  p drive number / file id
;       d7 cr   amount to buffer / unbuffer
;       a1 cr   buffer address
;       a2   s  internal buffer address
;       a3 c  p linkage block address
;       a4 c  p pointer to physical definition
;       a5 c  p pointer to map
;
;       status return standard
;---
iou_fmul

; loop unbuffering blocks

iofm_next
        jsr     iod_lcbf(a3)             ; locate buffer
        bne.s   iofm_rts                 ; ... oops

        cmp.l   d3,d7                    ; all to be read?
        bhs.s   iofm_unbf                ; ... yes
        move.l  d7,d3

; unbuffer d3 bytes

iofm_unbf
        move.l  a1,d0                    ; destination address
        or.w    d5,d0                    ; or source address (ish)

        add.l   d3,d1                    ; more read (or will be)
        add.l   d3,d5                    ; new buffer pointer
        sub.l   d3,d7                    ; amount left

        lsr.w   #1,d0                    ; odd?
        bcs.s   iofm_bend                ; ... yes, just copy bytes

        ror.l   #5,d3                    ; copy 8 long words at a time
        bra.s   iofm_lwend

iofm_lword
        move.l  (a2)+,(a1)+              ; copy long word
        move.l  (a2)+,(a1)+              ; copy long word
        move.l  (a2)+,(a1)+              ; copy long word
        move.l  (a2)+,(a1)+              ; copy long word
        move.l  (a2)+,(a1)+              ; copy long word
        move.l  (a2)+,(a1)+              ; copy long word
        move.l  (a2)+,(a1)+              ; copy long word
        move.l  (a2)+,(a1)+              ; copy long word
iofm_lwend
        dbra    d3,iofm_lword

        clr.w   d3
        rol.l   #5,d3                    ; amount remaining
        bra.s   iofm_bend

iofm_byte
        move.b  (a2)+,(a1)+              ; bytes at a time
iofm_bend
        dbra    d3,iofm_byte

        move.l  d7,d0                    ; any more to go?
        bne.s   iofm_next                ; ... yes
iofm_rts
        rts
        page
;+++
; Unbuffer up to d7 bytes, stopping at <NL>, from file d6 position d5
;
;       d1  u   byte count
;       d3   s
;       d4   s  block / byte buffer position
;       d5 cr   file position
;       d6 c  p drive number / file id
;       d7 cr   amount to buffer / unbuffer
;       a1 cr   buffer address
;       a2   s  internal buffer address
;       a3 c  p linkage block address
;       a4 c  p pointer to physical definition
;       a5 c  p pointer to map
;
;       status return standard
;---
iou_flin

; loop unbuffering but checking for 'enter'

iofl_next
        jsr     iod_lcbf(a3)             ; locate buffer
        bne.s   iofl_rts                 ; ... oops

        cmp.l   d3,d7                    ; all to be read?
        bhs.s   iofl_unbf                ; ... yes
        move.l  d7,d3

; unbuffer up to d3 bytes

iofl_unbf
        move.l  d3,d0                    ; max amount to copy
        beq.s   iofl_teof                ; ... none, buffer full
        bra.s   iofl_bend

iofl_byte
        move.b  (a2)+,(a1)               ; copy byte
        cmp.b   #k.enter,(a1)+           ; was it enter?
iofl_bend
        dbeq    d0,iofl_byte             ; next

        beq.s   iofl_done                ; ... enter found

        add.l   d3,d1                    ; more read
        add.l   d3,d5                    ; update pointer
        sub.l   d3,d7                    ; any more?
        bne.s   iofl_next                ; ... yes

iofl_teof
        moveq   #err.bffl,d0             ; buffer full
        cmp.l   d1,d2                    ; all read?
        beq.s   iofl_rts
        moveq   #err.eof,d0              ; ... no, must have been end of file
        bra.s   iofl_rts

iofl_done
        sub.w   d0,d3                    ; amount actually read
        add.l   d3,d1
        add.l   d3,d5                    ; update pointer
        moveq   #0,d0                    ; done
iofl_rts
        rts

        page
;+++
; Set directory entry (atomic) d7 bytes starting from the start
; of this file's entry. This entry avoids checking the end of file and
; always locates rather than allocates the appropriate sector. CHN_SDPS and
; CHN_SDID must be valid.
;
;       d1  u   byte count
;       d5    p file position
;       d6 c  p drive number / (file id)
;       d7 cr   amount to buffer / unbuffer
;       a0 c  p pointer to channel block
;       a1 cr   buffer address
;       a2   s  internal buffer address
;       a3 c  p linkage block address
;       a4 c  p pointer to physical definition
;       a5 c  p pointer to map
;
;       status return standard
;---
iou_sde0
        movem.l d3/d4,-(sp)              ; save d3/d4
        moveq   #0,d4
        bsr.s   iou_sden
        movem.l (sp)+,d3/d4
        rts
;+++
; Set directory entry (atomic) d7 bytes starting d4 bytes on from the start
; of this file's entry. This entry avoids checking the end of file and
; always locates rather than allocates the appropriate sector. CHN_SDPS and
; CHN_SDID must be valid.
;
;       d1  u   byte count
;       d3   s
;       d4 c s  byte position (long)
;       d5    p file position
;       d6 c  p drive number / (file id)
;       d7 cr   amount to buffer / unbuffer
;       a0 c  p pointer to channel block
;       a1 cr   buffer address
;       a2   s  internal buffer address
;       a3 c  p linkage block address
;       a4 c  p pointer to physical definition
;       a5 c  p pointer to map
;
;       status return standard
;---
iou_sden
        tst.b   iod_wprt(a4)             ; read only?
        bne.s   ibf_rdon                 ; ... yes

        movem.l d5/d6,-(sp)
        move.l  chn_sdps(a0),d5          ; start
        add.l   d4,d5
        move.w  chn_sdid(a0),d6          ; sub-directory
isd_retry
        jsr     iod_lcbf(a3)             ; locate buffer
        bne.s   isd_check
        move.l  d7,d3
        bsr.s   iosm_unbf                ; send multiple bytes
isd_check
        cmp.w   #err.nc,d0               ; is it not complete?
        beq.s   isd_retry                ; ... yes (or no??)
        tst.l   d0
        movem.l (sp)+,d5/d6
        rts

;+++
; Set multiple bytes and wait, d7 bytes to directory starting at byte d5.
; Assumes that directory entries cannot span sector boundries. CHN_FEOF
; must be valid.
;
;       d1  u   byte count
;       d4    p block / byte buffer position
;       d5 cr   file position
;       d6 c  p drive number / directory file ID
;       d7 cr   amount to buffer / unbuffer
;       a0 c  p pointer to channel block (for CSB)
;       a1 cr   buffer address
;       a2   s  internal buffer address
;       a3 c  p linkage block address
;       a4 c  p pointer to physical definition
;       a5 c  p pointer to map
;
;       status return standard
;---
iou_smlw
        movem.l d3/d4,-(sp)
isw_retry
        bsr.s   iou_smul                 ; send multiple bytes
        cmp.w   #err.nc,d0               ; is it not complete?
        beq.s   isw_retry                ; ... yes (or no??)
        movem.l (sp)+,d3/d4
        tst.l   d0
        rts

ibf_rdon
        moveq   #err.rdo,d0
        rts

;+++
; Send d7 bytes to file d6 starting at byte d5. CHN_FEOF must be valid.
;            
;       d1  u   byte count
;       d3   s
;       d4   s  block / byte buffer position
;       d5 cr   file position
;       d6 c  p drive number / file id
;       d7 cr   amount to buffer / unbuffer
;       a0 c  p channel block (for csb)
;       a1 cr   buffer address
;       a2   s  internal buffer address
;       a3 c  p linkage block address
;       a4 c  p pointer to physical definition
;       a5 c  p pointer to map
;
;       status return standard
;---
iou_smul
        tst.b   iod_wprt(a4)             ; read only?
        bne.s   ibf_rdon

; loop buffering blocks

iosm_next
        jsr     iod_albf(a3)             ; locate / allocate buffer
        bne.s   iosm_rts                 ; ... oops

        cmp.l   d3,d7                    ; all to be sent?
        bhs.s   iosm_unbf                ; ... yes
        move.l  d7,d3

; buffer d3 bytes

iosm_unbf
        move.l  a1,d0                    ; source
        or.w    d5,d0                    ; or destination

        add.l   d3,d1                    ; more sent (or will be)
        add.w   d3,d4                    ; new buffer pointer
        add.l   d3,d5                    ; new file pointer
        sub.l   d3,d7                    ; amount left

        lsr.w   #1,d0                    ; source or destination odd?
        bcs.s   iosm_bend                ; ... yes, just copy bytes

        ror.l   #5,d3                    ; copy 8 long words at a time
        bra.s   iosm_lwend

iosm_lword
        move.l  (a1)+,(a2)+              ; copy long word
        move.l  (a1)+,(a2)+              ; copy long word
        move.l  (a1)+,(a2)+              ; copy long word
        move.l  (a1)+,(a2)+              ; copy long word
        move.l  (a1)+,(a2)+              ; copy long word
        move.l  (a1)+,(a2)+              ; copy long word
        move.l  (a1)+,(a2)+              ; copy long word
        move.l  (a1)+,(a2)+              ; copy long word
iosm_lwend
        dbra    d3,iosm_lword

        clr.w   d3
        rol.l   #5,d3                    ; amount remaining
        bra.s   iosm_bend

iosm_byte
        move.b  (a1)+,(a2)+              ; copy byte
iosm_bend
        dbra    d3,iosm_byte
        jsr     iod_upbf(a3)             ; mark updated buffer
        move.l  d7,d0                    ; any more to go?
        bne.s   iosm_next                ; ... yes
iosm_rts
        rts
        end
