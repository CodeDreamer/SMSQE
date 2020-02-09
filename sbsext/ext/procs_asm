; SBSEXT_TK2_PROCS - Standard TK2 Procedures V3.01    1992  Tony Tebby
;
; 2018-07-24  3.01  Added SUSJB, FDEL (pjw)

	section procs

	xdef	tk2_ext_proc
	xdef	tk2_procs

	xref	ut_procdef

	include 'dev8_mac_proc'

tk2_ext_proc
	proc_stt
	proc_ref TK2_EXT
	proc_end

	proc_stt
	proc_end

tk2_ext
	lea	tk2_procs,a1
	jmp	ut_procdef

tk2_procs
	proc_stt

    GENIF I_ED = 1
	proc_def ED			; window based editor
    ENDGEN
	proc_def VIEW			; view a file
	proc_def DATA_USE		; directory control
	proc_def PROG_USE
	proc_def DEST_USE
	proc_def SPL_USE
	proc_def DDOWN
	proc_def DUP
	proc_def DNEXT
	proc_def DLIST
	proc_def MAKE_DIR
	proc_def DIR			; filing system maintenance
	proc_def WDIR
	proc_def STAT
	proc_def WSTAT
	proc_def DELETE
	proc_def WDEL
	proc_def COPY
	proc_def COPY_O
	proc_def COPY_N
	proc_def COPY_H
	proc_def WCOPY
	proc_def SPL
	proc_def SPLF
	proc_def RENAME
	proc_def WREN
	proc_def SET_FUPDT
	proc_def SET_FBKDT
	proc_def SET_FVERS
	proc_def LRESPR 		; file load and save
	proc_def CALL,call
	proc_def LBYTES
	proc_def SBYTES
	proc_def SBYTES_O
	proc_def SEXEC
	proc_def SEXEC_O
	proc_def CLEAR			; BASIC execution control
	proc_def NEW
	proc_def STOP
	proc_def RUN
	proc_def DO			; BASIC load and save
	proc_def LRUN
	proc_def LOAD
	proc_def MRUN
	proc_def MERGE
	proc_def SAVE
	proc_def SAVE_O
	proc_def EX,exsb		; program execution
	proc_def EXEC,exsb
	proc_def EW,ewsb
	proc_def EXEC_W,ewsb
	proc_def ET,etsb
	proc_def EX_M,emsb
	proc_def OPEN			; channel open/close
	proc_def OPEN_IN
	proc_def OPEN_NEW
	proc_def OPEN_OVER
	proc_def OPEN_DIR
	proc_def CLOSE
	proc_def GET
	proc_def BGET			; direct access file handling
	proc_def WGET
	proc_def LGET
	proc_def HGET
	proc_def PUT
	proc_def BPUT
	proc_def UPUT
	proc_def WPUT
	proc_def LPUT
	proc_def HPUT
	proc_def TRUNCATE
	proc_def FLUSH
	proc_def JOBS			; job control
	proc_def RJOB
	proc_def SPJOB
	proc_def SUSJB
	proc_def AJOB
	proc_def RECHP			; common heap handling
	proc_def CLCHP
	proc_def DEL_DEFB
	proc_def PRINT_USING		; formatted print
	proc_def CURSEN 		; screen handling
	proc_def CURDIS
;*	  proc_def CHAR_DEF
	proc_def CHAR_USE
	proc_def CHAR_INC
	proc_def WMON
	proc_def WTV
	proc_def REPORT 		; error handling
	proc_def CONTINUE
	proc_def RETRY
    GENIF I_AKEY = 1
	proc_def ALTKEY
    ENDGEN
    GENIF I_NET = 1
	proc_def FSERVE 		; file server
	proc_def NFS_USE
    ENDGEN
	proc_def EXTRAS 		; odds
	proc_end

	proc_stt
	proc_def PROGD$ 		; directory control
	proc_def DATAD$
	proc_def DESTD$
	proc_def FMAKE_DIR
	proc_def FDEL
	proc_def FTEST			; channel open
	proc_def FOPEN
	proc_def FOP_IN
	proc_def FOP_NEW
	proc_def FOP_OVER
	proc_def FOP_DIR
	proc_def FPOS			; direct access files
	proc_def FLEN			; file header information
	proc_def FTYP
	proc_def FDAT
	proc_def FXTRA
	proc_def FNAME$
	proc_def FUPDT
	proc_def FBKDT
	proc_def FVERS
	proc_def PJOB			; job control
	proc_def OJOB
	proc_def JOB$
	proc_def JOBID			; (v. 2.07)
	proc_def NXJOB
	proc_def ALCHP			; common heap handling
	proc_def FREE_MEM
	proc_def FDEC$			; numeric conversions
	proc_def IDEC$
	proc_def CDEC$
	proc_def FEXP$
	proc_def HEX$
	proc_def BIN$
	proc_def HEX
	proc_def BIN
	proc_def PARTYP 		; BASIC parameter
	proc_def PARUSE
	proc_def PARNAM$
	proc_def PARSTR$
	proc_def ERR_DF 		; error processing
	proc_def FEX,fexsb		; program execution as functions
	proc_def FEW,fewsb
	proc_def FET,fetsb
	proc_def FEX_M,femsb
	proc_def EXF,fexsb
	proc_end

	end
