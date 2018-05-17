; DV3 File Buffer Handling   V3.00    1992   Tony Tebby
;
; These are for use by the format dependent routines.
; They are the file analogues of dv3_drloc / dv3_drnew
; They are derived from dv3_albf and dv3_lcbf

        section dv3

        xdef    dv3_fbnew
        xdef    dv3_fbloc
        xdef    dv3_fbupd

        xref    dv3_sbloc
        xref    dv3_sbnew
        xref    dv3_slbupd

        include 'dev8_dv3_keys'
        include 'dev8_keys_err'
        include 'dev8_mac_assert'

;+++
; DV3 create new file buffer.
; If necessary it extends the file allocation.
;
;       d5 c  p file sector
;       d6 c  p file ID
;       d7 c  p drive number
;       a0 c  p channel block
;       a2  r   pointer to buffer
;       a3 c  p linkage block
;       a4 c  p definition block
;       a5 c  p pointer to map
;
;       status return standard
;---
dv3_fbnew
dfb.reg reg     d1/d2/d3/a1
        movem.l dfb.reg,-(sp)

        move.l  d5,d3                    ; sector number
        divu    ddf_asect(a4),d3         ; this can deal with group up to 65535
        moveq   #0,d2
        move.w  d3,d2
        assert  d3c_dgroup,d3c_fgroup-4
        movem.l d3c_dgroup(a0),d0/d1     ; current drive and file group
        cmp.l   d3,d2                    ; first sector of group?
        bne.s   dfn_loc                  ; ... no, locate
        jsr     ddf_salloc(a4)           ; allocate new group
        bge.s   dfn_sgrp
        bra.s   dfn_exit
dfn_loc
        cmp.w   d1,d2                    ; current group?
        beq.s   dfn_new                  ; ... yes, just get new slave block
        jsr     ddf_slocate(a4)          ; locate this group
        blt.s   dfn_exit
dfn_sgrp
        assert  d3c_dgroup,d3c_fgroup-4
        movem.l d0/d2,d3c_dgroup(a0)     ; new current drive and file group

dfn_new
        move.w  d0,d3                    ; drive sector / group
        lea     d3c_csb(a0),a2
        jsr     dv3_sbnew                ; find new slave block
dfn_exit
        movem.l (sp)+,dfb.reg
        rts

;+++
; DV3 locate a file buffer.
; If necessary, it reads the appropriate sector from the disk
;
;       d5 c  p file sector
;       d6 c  p file ID
;       d7 c  p drive number
;       a0 c  p channel block
;       a2  r   pointer to buffer
;       a3 c  p linkage block
;       a4 c  p definition block
;       a5 c  p pointer to map
;
;       status return standard
;---
dv3_fbloc
        movem.l dfb.reg,-(sp)

        lea     d3c_csb(a0),a2
        jsr     dv3_sbloc                ; locate slave block
        ble.s   dfl_exit

        move.l  d5,d3                    ; sector number
        divu    ddf_asect(a4),d3         ; this can deal with group up to 65535
        moveq   #0,d2
        move.w  d3,d2
        assert  d3c_dgroup,d3c_fgroup-4
        lea     d3c_fgroup(a0),a1
        move.l  (a1),d1
        move.l  -(a1),d0                 ; current drive and file group
        beq.s   dfl_loc2                 ; none yet
        cmp.w   d1,d2                    ; current group?
        beq.s   dfl_new                  ; ... yes, new slave block required

dfl_loc2
        jsr     ddf_slocate(a4)
        blt.s   dfl_exit
        assert  d3c_dgroup,d3c_fgroup-4
        movem.l d0/d2,d3c_dgroup(a0)    ; new current drive and file group

dfl_new
        move.w  d0,d3                    ; drive sector /  group
        lea     d3c_csb(a0),a2
        jsr     dv3_sbnew                ; find new slave block

        addq.b  #sbt.read-sbt.true,sbt_stat(a1) ; read requested
        jsr     ddl_slbfill(a3)          ; read sector

dfl_exit
        movem.l (sp)+,dfb.reg
        rts

;+++
; DV3 mark a file buffer (d3c_csb) updated .
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
dv3_fbupd
        move.l  a1,-(sp)
        jsr     dv3_slbupd
        move.l  (sp)+,a1
        rts

        end
