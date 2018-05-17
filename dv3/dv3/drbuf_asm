; DV3 Directory Buffer Handling   V3.00    1992   Tony Tebby

        section dv3

        xdef    dv3_drnew
        xdef    dv3_drloc
        xdef    dv3_drupd

        xref    dv3_sbloc
        xref    dv3_sbnew
        xref    dv3_a1upd

        include 'dev8_dv3_keys'
        include 'dev8_keys_err'
        include 'dev8_mac_assert'

;+++
; DV3 allocate a new directory block for extending the directory.
;
;       d5 c  p directory sector
;       d7 c  p drive number
;       a0 c  p channel block
;       a2  r   pointer to buffer
;       a3 c  p linkage block
;       a4 c  p definition block
;       a5 c  p pointer to map
;
;       status return standard
;---
dv3_drnew
ddn.reg reg     d1/d2/d3/d6/a1
        movem.l ddn.reg,-(sp)
        move.l  d3c_sdid(a0),d6

        move.l  d5,d3                    ; sector number
        divu    ddf_asect(a4),d3         ; this can deal with group up to 65535
        moveq   #0,d2
        move.w  d3,d2
        moveq   #0,d0                    ; no current group *** we could look at sdsb
        cmp.l   d3,d2                    ; first sector of group?
        bne.s   ddn_loc                  ; ... no, locate group
        jsr     ddf_salloc(a4)           ; ... yes, allocate
        bge.s   ddn_new
        bra.s   ddn_exit

ddn_loc
        jsr     ddf_slocate(a4)          ; locate group
        blt.s   ddn_exit

ddn_new
        move.w  d0,d3                    ; drive sector / group
        lea     d3c_sdsb(a0),a2
        jsr     dv3_sbnew                ; find new slave block

ddn_exit
        movem.l (sp)+,ddn.reg
        rts

;+++
; DV3 locate a directory buffer.
;
;       d5 c  p directory sector
;       d7 c  p drive number
;       a0 c  p channel block
;       a2  r   pointer to buffer
;       a3 c  p linkage block
;       a4 c  p definition block
;       a5 c  p pointer to map
;
;       status return standard
;---
dv3_drloc
        movem.l ddn.reg,-(sp)
        move.l  d3c_sdid(a0),d6

        lea     d3c_sdsb(a0),a2
        jsr     dv3_sbloc                ; locate slave block
        ble.s   dlc_exit

        move.l  d5,d3                    ; sector number
        divu    ddf_asect(a4),d3         ; this can deal with group up to 65535
        moveq   #0,d2
        move.w  d3,d2
        moveq   #0,d0                    ; no current group *** we could look at sb

        jsr     ddf_slocate(a4)          ; locate group
        blt.s   dlc_exit

        move.w  d0,d3                    ; drive sector / group
        lea     d3c_sdsb(a0),a2
        jsr     dv3_sbnew                ; find new slave block

        addq.b  #sbt.read-sbt.true,sbt_stat(a1) ; read requested
        jsr     ddl_slbfill(a3)          ; read sector

dlc_exit
        movem.l (sp)+,ddn.reg
        rts

;+++
; DV3 mark a directory buffer updated.
;
;       d7 c  p drive number
;       a0 c  p channel block
;       a3 c  p linkage block
;       a4 c  p definition block
;       a5 c  p pointer to map
;
;       all registers preserved
;       status according to d0
;---
dv3_drupd
        move.l  a1,-(sp)
        move.l  d3c_sdsb(a0),a1
        jsr     dv3_a1upd
        move.l  (sp)+,a1
        rts
        end
